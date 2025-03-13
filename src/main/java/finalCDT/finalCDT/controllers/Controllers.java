package finalCDT.finalCDT.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import finalCDT.finalCDT.dto.UserDTO;
import finalCDT.finalCDT.dto.UserResponseDTO;
import finalCDT.finalCDT.entitys.Account;
import finalCDT.finalCDT.entitys.Cdt;
import finalCDT.finalCDT.entitys.TarjetaCredito;
import finalCDT.finalCDT.entitys.Transfer;
import finalCDT.finalCDT.helpers.ApiResponse;
import finalCDT.finalCDT.services.UserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class Controllers {

    @Autowired
    private UserService userService;

    List<Account> accounts = new ArrayList<>();
    List<Transfer> transfers = new ArrayList<>();
    List<Cdt> cdts = new ArrayList<>();
    List<TarjetaCredito> tarjetasCreditos = new ArrayList<>();

    private <T> Map<String, Object> setApiResponse(ApiResponse<T> apiResponse) {
        Map<String, Object> response = new HashMap<>();
        response.put("ok", apiResponse.isOk());
        response.put("data", apiResponse.getData());
        response.put("message", apiResponse.getMessage());
        return response;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        // Validar campos obligatorios
        if (userDTO.getCc() == null || userDTO.getCc().trim().isEmpty() ||
            userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty() ||
            userDTO.getLastname() == null || userDTO.getLastname().trim().isEmpty() ||
            userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Todos los campos son obligatorios")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar duplicados
        if (userService.getUserByCc(userDTO.getCc()) != null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "La cc ya existe")), 
                HttpStatus.BAD_REQUEST
            );
        }

        if (userService.getUserByEmail(userDTO.getEmail()) != null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "El email ya existe")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Crear usuario
        UserResponseDTO savedUser = userService.createUser(userDTO);
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, savedUser, "Usuario guardado")), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/users")
    public ResponseEntity<?> usersActivated() {
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, userService.getAllActiveUsers(), "Lista de usuarios activos")), 
            HttpStatus.OK
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> userId(@PathVariable UUID id) {
        try {
            UserResponseDTO userEncontrado = userService.getUserById(id);
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(true, userEncontrado, "Usuario encontrado")), 
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se encontró el usuario")), 
                HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/users/allusers")
    public ResponseEntity<?> listarUsuarios() {
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, userService.getAllUsers(), "Esta es la lista de usuarios")), 
            HttpStatus.OK
        );
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> putUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        // Validar campos obligatorios
        if (userDTO.getCc() == null || userDTO.getCc().trim().isEmpty() ||
            userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty() ||
            userDTO.getLastname() == null || userDTO.getLastname().trim().isEmpty() ||
            userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Los campos son obligatorios para actualizar")), 
                HttpStatus.BAD_REQUEST
            );
        }

        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(true, updatedUser, "Usuario actualizado")), 
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, e.getMessage())), 
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable UUID id) {
        try {
            userService.deactivateUser(id);
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(true, null, "Usuario desactivado")), 
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, e.getMessage())), 
                HttpStatus.NOT_FOUND
            );
        }
    }

    /// ACCOUNT ///

    @PostMapping("/accounts")
    public ResponseEntity<?> postAccount(@RequestBody Account cuenta) {
        if (cuenta.getBalance() <= 0 || cuenta.getNumber() == null || cuenta.getNumber().trim().isEmpty() ||
            cuenta.getType() == null || cuenta.getType().trim().isEmpty() || cuenta.getIdUser() == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Todos los datos son obligatorios")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que el tipo sea válido
        String tipo = cuenta.getType().toLowerCase().trim();
        if (!tipo.equals("ahorro") && !tipo.equals("corriente")) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Solo se puede ahorro o corriente")), 
                HttpStatus.BAD_REQUEST
            );
        }

        accounts.add(cuenta);
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, accounts, "Cuenta bancaria creada")), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/accounts/activas")
    public ResponseEntity<?> listarCuentas() {
        Map<String, Object> response = new HashMap<>();

        Account cuentasActivadas = accounts.stream().filter(item -> item.getIsActivated().equals(true)).findAny()
                .orElse(null);

        if (cuentasActivadas == null) {
            response = setApiResponse(new ApiResponse<>(false, null, "No se encontraron cuentas activas"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, cuentasActivadas, "Cuentas activadas"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> cuentaId(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEncontrada = accounts.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (cuentaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, null, "No se encontró la cuenta"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, cuentaEncontrada, "Cuenta encontrada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> listarCunetas() {
        Map<String, Object> response = new HashMap<>();

        response = setApiResponse(new ApiResponse<>(true, accounts, "Lista de cuentas bancarias"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> putAccount(@PathVariable UUID id, @RequestBody Account cuenta) {
        Account cuentaEncontrada = null;
        for (Account acc : accounts) {
            if (acc.getId().equals(id)) {
                cuentaEncontrada = acc;
                break;
            }
        }

        if (cuentaEncontrada == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se encontró la cuenta")), 
                HttpStatus.NOT_FOUND
            );
        }

        if (cuenta.getBalance() <= 0 || cuenta.getNumber() == null || cuenta.getNumber().trim().isEmpty() ||
            cuenta.getType() == null || cuenta.getType().trim().isEmpty()) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Todos los datos son obligatorios")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que no se cambie el número de cuenta
        if (!cuentaEncontrada.getNumber().equals(cuenta.getNumber())) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se puede cambiar el número de cuenta")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que no se cambie el ID del usuario
        if (!cuentaEncontrada.getIdUser().equals(cuenta.getIdUser())) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se puede cambiar el id del usuario")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que el tipo sea válido
        String tipo = cuenta.getType().toLowerCase().trim();
        if (!tipo.equals("ahorro") && !tipo.equals("corriente")) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Solo se puede ahorro o corriente")), 
                HttpStatus.BAD_REQUEST
            );
        }

        cuentaEncontrada.setBalance(cuenta.getBalance());
        cuentaEncontrada.setType(cuenta.getType());
        
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "Cuenta actualizada")), 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        Account cuentaEncontrada = null;
        for (Account acc : accounts) {
            if (acc.getId().equals(id)) {
                cuentaEncontrada = acc;
                break;
            }
        }

        if (cuentaEncontrada == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se encontró la cuenta")), 
                HttpStatus.NOT_FOUND
            );
        }

        // Desactivar la cuenta en lugar de eliminarla
        cuentaEncontrada.setIsActivated(false);
        
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "Cuenta desactivada exitosamente")), 
            HttpStatus.OK
        );
    }

    /// TRANSFERENCIAS ///

    @GetMapping("/transfers")
    public ResponseEntity<?> listarTransferencias() {
        Map<String, Object> response = new HashMap<>();

        response = setApiResponse(new ApiResponse<>(true, transfers, "Lista de transferencias"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<?> transferrenciaId(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Transfer transferenciaEncontrada = transfers.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElse(null);

        if (transferenciaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, null, "No se encontró la transferencia"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }

        response = setApiResponse(new ApiResponse<>(true, transferenciaEncontrada, "Transferencia encontrada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> realizarTransferencia(@RequestBody Transfer transfer) {
        try {
            // Validar monto de transferencia
            if (transfer.getAmount() <= 0) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, "El monto de la transferencia debe ser mayor a 0")), 
                    HttpStatus.BAD_REQUEST
                );
            }

            // Validar que las cuentas sean diferentes
            if (transfer.getOriginAccount() == 0 || transfer.getDestinationAccount() == 0) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, "Las cuentas de origen y destino son requeridas")), 
                    HttpStatus.BAD_REQUEST
                );
            }

            if (transfer.getOriginAccount() == transfer.getDestinationAccount()) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, "La cuenta de origen y destino no pueden ser la misma")), 
                    HttpStatus.BAD_REQUEST
                );
            }

            // Buscar cuenta de origen
            Account originAccount = null;
            for (Account acc : accounts) {
                if (acc.getId() != null && acc.getId().equals(transfer.getOriginAccount()) && acc.getIsActivated()) {
                    originAccount = acc;
                    break;
                }
            }

            if (originAccount == null) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, "Cuenta bancaria de origen no encontrada o inactiva")), 
                    HttpStatus.BAD_REQUEST
                );
            }

            // Buscar cuenta de destino
            Account destinationAccount = null;
            for (Account acc : accounts) {
                if (acc.getId() != null && acc.getId().equals(transfer.getDestinationAccount()) && acc.getIsActivated()) {
                    destinationAccount = acc;
                    break;
                }
            }

            if (destinationAccount == null) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, "Cuenta bancaria de destino no encontrada o inactiva")), 
                    HttpStatus.BAD_REQUEST
                );
            }

            // Validar saldo suficiente
            if (originAccount.getBalance() < transfer.getAmount()) {
                return new ResponseEntity<>(
                    setApiResponse(new ApiResponse<>(false, null, 
                        String.format("Saldo insuficiente. Saldo actual: %.2f, Monto a transferir: %.2f", 
                            originAccount.getBalance(), transfer.getAmount()))), 
                    HttpStatus.BAD_REQUEST
                );
            }

            // Realizar transferencia
            originAccount.setBalance(originAccount.getBalance() - transfer.getAmount());
            destinationAccount.setBalance(destinationAccount.getBalance() + transfer.getAmount());
            transfers.add(transfer);

            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(true, transfer, 
                    String.format("Transferencia exitosa. Nuevo saldo: %.2f", originAccount.getBalance()))), 
                HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Error al procesar la transferencia: " + e.getMessage())), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /// CDT ///

    @GetMapping("/cdt")
    public ResponseEntity<?> listarCdts() {
        Map<String, Object> response = new HashMap<>();
        response = setApiResponse(new ApiResponse<>(true, cdts, "Lista de cdts"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cdt/{id}")
    public ResponseEntity<?> cdtId(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Cdt cdtEncontrado = cdts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cdtEncontrado == null) {
            response = setApiResponse(new ApiResponse<>(false, null, "Cdt no encontrado"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, cdtEncontrado, "Cdt encontrado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cdt")
    public ResponseEntity<?> crearCdt(@RequestBody Cdt cdt) {
        if (cdt.getMonto() <= 0 || cdt.getPlazoMeses() <= 0 || 
            cdt.getIdUser() == 0) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Todos los datos son obligatorios y deben ser valores positivos")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que la cuenta exista y esté activa
        Account cuenta = null;
        for (Account acc : accounts) {
            if (acc.getId() != null && acc.getId().equals(cdt.getIdUser()) && acc.getIsActivated()) {
                cuenta = acc;
                break;
            }
        }

        if (cuenta == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "La cuenta no existe o está inactiva")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar saldo suficiente
        if (cuenta.getBalance() < cdt.getMonto()) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Saldo insuficiente en la cuenta")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Descontar el monto del CDT de la cuenta
        cuenta.setBalance(cuenta.getBalance() - cdt.getMonto());
        cdts.add(cdt);

        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, cdts, "CDT creado exitosamente")), 
            HttpStatus.CREATED
        );
    }

    @PutMapping("/cdt/{id}")
    public ResponseEntity<?> actualizarCdt(@PathVariable UUID id, @RequestBody Cdt cdt) {
        Cdt cdtEncontrado = null;
        for (Cdt c : cdts) {
            if (c.getId() != null && c.getId().equals(id)) {
                cdtEncontrado = c;
                break;
            }
        }

        if (cdtEncontrado == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "CDT no encontrado")), 
                HttpStatus.NOT_FOUND
            );
        }

        if (cdt.getMonto() <= 0 || cdt.getPlazoMeses() <= 0) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Los datos son obligatorios y deben ser valores positivos")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // No permitir cambios en el ID de usuario
        if (cdtEncontrado.getIdUser() != cdt.getIdUser()) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se puede cambiar el usuario asociado al CDT")), 
                HttpStatus.BAD_REQUEST
            );
        }

        cdtEncontrado.setMonto(cdt.getMonto());
        cdtEncontrado.setPlazoMeses(cdt.getPlazoMeses());

        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "CDT actualizado exitosamente")), 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/cdt/{id}")
    public ResponseEntity<?> deleteCdt(@PathVariable UUID id) {
        Cdt cdtEncontrado = null;
        for (Cdt c : cdts) {
            if (c.getId().equals(id)) {
                cdtEncontrado = c;
                break;
            }
        }

        if (cdtEncontrado == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "CDT no encontrado")), 
                HttpStatus.NOT_FOUND
            );
        }

        cdts.remove(cdtEncontrado);
        
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "CDT eliminado exitosamente")), 
            HttpStatus.OK
        );
    }

    /// TARJETA DE CREDITO ///

    @GetMapping("/tarjetaCredito")
    public ResponseEntity<?> listarTarjetasCredito() {
        Map<String, Object> response = new HashMap<>();
        response = setApiResponse(new ApiResponse<>(true, tarjetasCreditos, "Estas son las tarjetas de credito"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tarjetaCredito/{id}")
    public ResponseEntity<?> listarById(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        TarjetaCredito tarejtaEncontrada = tarjetasCreditos.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElse(null);

        if (tarejtaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, null, "No se encontró la tarjeta de credito"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, tarejtaEncontrada, "Tarjeta encontrada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tarjetas")
    public ResponseEntity<?> postTarjetaCredito(@RequestBody TarjetaCredito tarjeta) {
        if (tarjeta.getNombre() == null || tarjeta.getNombre().trim().isEmpty() || 
            tarjeta.getApellido() == null || tarjeta.getApellido().trim().isEmpty() || 
            tarjeta.getEdad() <= 0 || tarjeta.getIdUser() == 0) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Los datos son obligatorios")), 
                HttpStatus.BAD_REQUEST
            );
        }

        tarjetasCreditos.add(tarjeta);
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, tarjetasCreditos, "Tarjeta de crédito agregada")), 
            HttpStatus.CREATED
        );
    }

    @PutMapping("/tarjetas/{id}")
    public ResponseEntity<?> putTarjetaCredito(@PathVariable UUID id, @RequestBody TarjetaCredito tarjeta) {
        TarjetaCredito tarjetaEncontrada = null;
        for (TarjetaCredito t : tarjetasCreditos) {
            if (t.getId() != null && t.getId().equals(id)) {
                tarjetaEncontrada = t;
                break;
            }
        }

        if (tarjetaEncontrada == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se encontró la tarjeta de crédito")), 
                HttpStatus.NOT_FOUND
            );
        }

        if (tarjeta.getNombre() == null || tarjeta.getNombre().trim().isEmpty() || 
            tarjeta.getApellido() == null || tarjeta.getApellido().trim().isEmpty() || 
            tarjeta.getEdad() <= 0) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "Los datos son obligatorios")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que no se cambie el ID del usuario
        if (tarjetaEncontrada.getIdUser() != tarjeta.getIdUser()) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se puede cambiar el id del usuario")), 
                HttpStatus.BAD_REQUEST
            );
        }

        tarjetaEncontrada.setNombre(tarjeta.getNombre());
        tarjetaEncontrada.setApellido(tarjeta.getApellido());
        tarjetaEncontrada.setEdad(tarjeta.getEdad());
        
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "Tarjeta de crédito actualizada")), 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/tarjetaCredito/{id}")
    public ResponseEntity<?> deleteTarjetaCredito(@PathVariable UUID id) {
        TarjetaCredito tarejtaEncontrada = null;
        for (TarjetaCredito t : tarjetasCreditos) {
            if (t.getId().equals(id)) {
                tarejtaEncontrada = t;
                break;
            }
        }

        if (tarejtaEncontrada == null) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "No se encontró la tarjeta de credito")), 
                HttpStatus.NOT_FOUND
            );
        }

        tarjetasCreditos.remove(tarejtaEncontrada);
        
        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, null, "Tarjeta de credito eliminada exitosamente")), 
            HttpStatus.OK
        );
    }

    /// SIMULAR CDT ///

    @PostMapping("/cdt/simular")
    public ResponseEntity<?> simularCdt(@RequestBody Cdt cdt) {
        if (cdt.getMonto() <= 0 || cdt.getPlazoMeses() <= 0) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "El monto y el plazo deben ser valores positivos")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Validar que el plazo sea válido (3, 6, 9 o 12 meses)
        int plazo = cdt.getPlazoMeses();
        if (plazo != 3 && plazo != 6 && plazo != 9 && plazo != 12) {
            return new ResponseEntity<>(
                setApiResponse(new ApiResponse<>(false, null, "El plazo debe ser de 3, 6, 9 o 12 meses")), 
                HttpStatus.BAD_REQUEST
            );
        }

        // Calcular la tasa de interés según el plazo
        double tasaInteres;
        switch (plazo) {
            case 3:
                tasaInteres = 0.05;
                break;
            case 6:
                tasaInteres = 0.06;
                break;
            case 9:
                tasaInteres = 0.07;
                break;
            case 12:
                tasaInteres = 0.08;
                break;
            default:
                tasaInteres = 0.0;
        }

        // Calcular el interés y el monto final
        double interes = cdt.getMonto() * tasaInteres * (plazo / 12.0);
        double montoFinal = cdt.getMonto() + interes;

        Map<String, Double> simulacion = new HashMap<>();
        simulacion.put("montoInicial", cdt.getMonto());
        simulacion.put("tasaInteres", tasaInteres);
        simulacion.put("interes", interes);
        simulacion.put("montoFinal", montoFinal);

        return new ResponseEntity<>(
            setApiResponse(new ApiResponse<>(true, simulacion, "Simulación calculada exitosamente")), 
            HttpStatus.OK
        );
    }

    /// MONTO TOTAL DE LAS CUENTAS DE AHORRO ///

    @GetMapping("/monto/total")
    public ResponseEntity<?> montoTotalCuentas() {
        Map<String, Object> response = new HashMap<>();

        Double montototal = 0.0;

        for (Account item : accounts) {
            montototal += (item.getBalance());
        }

        response = setApiResponse(new ApiResponse<>(true, montototal, "Monto total de todas las cuentas de ahorro"));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /// MONTO TOTAL DE GANANCIAS CDT ///

    @GetMapping("/ganancias/totales")
    public ResponseEntity<?> gananciasTotales() {
        Map<String, Object> response = new HashMap<>();

        Double gananciatotal = 0.0;
        List<Double> ganancias = new ArrayList<>();

        for (Cdt item : cdts) {
            ganancias.add(item.getMonto() - (item.getMonto() * 0.04));
        }
        for (Double item : ganancias) {
            gananciatotal += (item);
        }

        response = setApiResponse(new ApiResponse<>(true, gananciatotal, "Ganancias totales del cdt"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}