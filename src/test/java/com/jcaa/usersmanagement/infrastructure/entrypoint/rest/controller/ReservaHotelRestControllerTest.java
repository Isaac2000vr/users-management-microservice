package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.application.port.in.CreateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteReservaHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllReservasHotelUseCase;
import com.jcaa.usersmanagement.application.port.in.GetReservaHotelByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateReservaHotelUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateReservaHotelCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetReservaHotelByIdQuery;
import com.jcaa.usersmanagement.domain.enums.ReservaHotelStatus;
import com.jcaa.usersmanagement.domain.model.ReservaHotelModel;
import com.jcaa.usersmanagement.domain.valueobject.ReservaHotelId;
import com.jcaa.usersmanagement.infrastructure.security.JwtAuthenticationFilter;
import com.jcaa.usersmanagement.infrastructure.security.JwtPrincipal;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenService;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import com.jcaa.usersmanagement.infrastructure.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = ReservaHotelRestController.class,
    properties = "spring.main.web-application-type=servlet")
@Import({
  SecurityConfig.class,
  JwtAuthenticationFilter.class,
  RestAuthenticationEntryPoint.class,
  RestAccessDeniedHandler.class
})
@DisplayName("ReservaHotelRestController")
class ReservaHotelRestControllerTest {

  private static final String ID = "res-001";

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateReservaHotelUseCase createReservaHotelUseCase;
  @MockBean private UpdateReservaHotelUseCase updateReservaHotelUseCase;
  @MockBean private DeleteReservaHotelUseCase deleteReservaHotelUseCase;
  @MockBean private GetReservaHotelByIdUseCase getReservaHotelByIdUseCase;
  @MockBean private GetAllReservasHotelUseCase getAllReservasHotelUseCase;
  @MockBean private JwtTokenService jwtTokenService;

  @Test
  @DisplayName("GET /api/reservas-hotel debe retornar todas las reservas")
  void shouldGetAllReservasThroughSpringMvc() throws Exception {
    final ReservaHotelModel reserva = buildSampleModel();
    when(getAllReservasHotelUseCase.execute()).thenReturn(List.of(reserva));
    when(jwtTokenService.parse("valid-token")).thenReturn(new JwtPrincipal("u-001", com.jcaa.usersmanagement.domain.enums.UserRole.REVIEWER));

    mockMvc
        .perform(get("/api/reservas-hotel").header("Authorization", "Bearer valid-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ID))
        .andExpect(jsonPath("$[0].hotel").value("Hotel Cartagena"))
        .andExpect(jsonPath("$[0].huesped").value("Juan Perez"))
        .andExpect(jsonPath("$[0].valor").value(450000.0))
        .andExpect(jsonPath("$[0].numAcompanantes").value(2))
        .andExpect(jsonPath("$[0].status").value("PENDING"));
  }

  @Test
  @DisplayName("POST /api/reservas-hotel debe crear una reserva válida")
  void shouldCreateReservaThroughSpringMvc() throws Exception {
    final String request =
        """
        {
          "id": "%s",
          "fecha": "2026-11-10",
          "hotel": "Hotel Cartagena",
          "huesped": "Juan Perez",
          "fechaInicio": "2026-12-01",
          "fechaFin": "2026-12-05",
          "valor": 450000.0,
          "habitacion": "301",
          "numAcompanantes": 2,
          "pais": "Colombia",
          "departamento": "Bolivar",
          "ciudad": "Cartagena",
          "horaCheckin": "15:00",
          "horaCheckout": "12:00",
          "empleadoAtiende": "Maria Lopez",
          "empleadoDespide": "Carlos Ruiz",
          "descripcion": "Reserva con vista al mar"
        }
        """.formatted(ID);
    final ReservaHotelModel reserva = buildSampleModel();
    when(createReservaHotelUseCase.execute(
            new CreateReservaHotelCommand(
                ID,
                "2026-11-10",
                "Hotel Cartagena",
                "Juan Perez",
                "2026-12-01",
                "2026-12-05",
                450000.0,
                "301",
                2,
                "Colombia",
                "Bolivar",
                "Cartagena",
                "15:00",
                "12:00",
                "Maria Lopez",
                "Carlos Ruiz",
                "Reserva con vista al mar")))
        .thenReturn(reserva);

    mockMvc
        .perform(post("/api/reservas-hotel").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.status").value("PENDING"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("PUT /api/reservas-hotel/{id} debe actualizar una reserva")
  void shouldUpdateReservaThroughSpringMvc() throws Exception {
    final String request =
        """
        {
          "fecha": "2026-11-10",
          "hotel": "Hotel Cartagena",
          "huesped": "Juan Perez",
          "fechaInicio": "2026-12-01",
          "fechaFin": "2026-12-05",
          "valor": 500000.0,
          "habitacion": "302",
          "numAcompanantes": 3,
          "pais": "Colombia",
          "departamento": "Bolivar",
          "ciudad": "Cartagena",
          "horaCheckin": "15:00",
          "horaCheckout": "12:00",
          "empleadoAtiende": "Maria Lopez",
          "empleadoDespide": "Carlos Ruiz",
          "descripcion": null,
          "status": "CONFIRMED"
        }
        """;
    final ReservaHotelModel reserva =
        new ReservaHotelModel(
            new ReservaHotelId(ID),
            "2026-11-10",
            "Hotel Cartagena",
            "Juan Perez",
            "2026-12-01",
            "2026-12-05",
            500000.0,
            "302",
            3,
            "Colombia",
            "Bolivar",
            "Cartagena",
            "15:00",
            "12:00",
            "Maria Lopez",
            "Carlos Ruiz",
            null,
            ReservaHotelStatus.CONFIRMED);
    when(updateReservaHotelUseCase.execute(
            new UpdateReservaHotelCommand(
                ID,
                "2026-11-10",
                "Hotel Cartagena",
                "Juan Perez",
                "2026-12-01",
                "2026-12-05",
                500000.0,
                "302",
                3,
                "Colombia",
                "Bolivar",
                "Cartagena",
                "15:00",
                "12:00",
                "Maria Lopez",
                "Carlos Ruiz",
                null,
                "CONFIRMED")))
        .thenReturn(reserva);

    mockMvc
        .perform(
            put("/api/reservas-hotel/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.habitacion").value("302"))
        .andExpect(jsonPath("$.status").value("CONFIRMED"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("DELETE /api/reservas-hotel/{id} debe eliminar una reserva")
  void shouldDeleteReservaThroughSpringMvc() throws Exception {
    mockMvc.perform(delete("/api/reservas-hotel/{id}", ID)).andExpect(status().isNoContent());

    verify(deleteReservaHotelUseCase).execute(new DeleteReservaHotelCommand(ID));
  }

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("GET /api/reservas-hotel/{id} debe responder usando el caso de uso inyectado por Spring")
  void shouldGetReservaByIdThroughSpringMvc() throws Exception {
    // Arrange
    final ReservaHotelModel reserva = buildSampleModel();
    when(getReservaHotelByIdUseCase.execute(new GetReservaHotelByIdQuery(ID))).thenReturn(reserva);

    // Act & Assert
    mockMvc
        .perform(get("/api/reservas-hotel/{id}", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.hotel").value("Hotel Cartagena"))
        .andExpect(jsonPath("$.huesped").value("Juan Perez"))
        .andExpect(jsonPath("$.pais").value("Colombia"))
        .andExpect(jsonPath("$.ciudad").value("Cartagena"))
        .andExpect(jsonPath("$.status").value("PENDING"));
  }

  @Test
  @DisplayName("POST /api/reservas-hotel debe rechazar una solicitud inválida antes del caso de uso")
  void shouldRejectInvalidCreateRequest() throws Exception {
    // Arrange — id vacío, hotel vacío y valor negativo
    final String invalidRequest =
        """
        {
          "id": "",
          "fecha": "2026-11-10",
          "hotel": "",
          "huesped": "Juan Perez",
          "fechaInicio": "2026-12-01",
          "fechaFin": "2026-12-05",
          "valor": -1,
          "habitacion": "301",
          "numAcompanantes": 2,
          "pais": "Colombia",
          "departamento": "Bolivar",
          "ciudad": "Cartagena",
          "horaCheckin": "15:00",
          "horaCheckout": "12:00",
          "empleadoAtiende": "Maria Lopez",
          "empleadoDespide": "Carlos Ruiz"
        }
        """;

    // Act & Assert
    mockMvc
        .perform(
            post("/api/reservas-hotel").contentType(MediaType.APPLICATION_JSON).content(invalidRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  @DisplayName("GET /api/reservas-hotel/{id} debe requerir autenticacion")
  void shouldRejectUnauthenticatedRequest() throws Exception {
    // Arrange

    // Act & Assert
    mockMvc
        .perform(get("/api/reservas-hotel/{id}", ID))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("GET /api/reservas-hotel/{id} debe rechazar un rol sin permisos")
  void shouldRejectUnauthorizedRole() throws Exception {
    // Arrange

    // Act & Assert
    mockMvc
        .perform(get("/api/reservas-hotel/{id}", ID))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403));
  }

  // ── helper

  private ReservaHotelModel buildSampleModel() {
    return ReservaHotelModel.create(
        new ReservaHotelId(ID),
        "2026-11-10",
        "Hotel Cartagena",
        "Juan Perez",
        "2026-12-01",
        "2026-12-05",
        450000.0,
        "301",
        2,
        "Colombia",
        "Bolivar",
        "Cartagena",
        "15:00",
        "12:00",
        "Maria Lopez",
        "Carlos Ruiz",
        "Reserva con vista al mar");
  }
}
