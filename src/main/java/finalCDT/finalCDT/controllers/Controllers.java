package finalCDT.finalCDT.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

import finalCDT.finalCDT.helpers.ApiResponse;
import finalCDT.finalCDT.models.Account;
import finalCDT.finalCDT.models.Cdt;
import finalCDT.finalCDT.models.TarjetaCredito;
import finalCDT.finalCDT.models.Transfer;
import finalCDT.finalCDT.models.User;

@RestController
@CrossOrigin(origins = "*")
public class Controllers {

    List<Account> accounts = new ArrayList<>();
    List<Transfer> transfers = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Cdt> cdts = new ArrayList<>();
    List<TarjetaCredito> tarjetasCreditos = new ArrayList<>();

    public <T, K> Map<String, Object> setApiResponse(ApiResponse<T, K> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("ok", data.isOk());
        response.put("data", data.getData());
        response.put("message", data.getMessage());
        return response;
    }

    @PostMapping("/user")
    public ResponseEntity<?> postUsers(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (user.getCc().equals(null) || user.getCc().isEmpty() || user.getEmail().equals(null)
                || user.getEmail().isEmpty() || user.getLastname().equals(null) || user.getLastname().isEmpty()
                || user.getName().equals(null) || user.getName().isEmpty()) {
            response = setApiResponse(new ApiResponse<>(false, "", "Todos los campos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User ccDuplicada = users.stream().filter(item -> item.getCc().equals(user.getCc())).findAny().orElse(null);

        if (ccDuplicada != null) {
            response = setApiResponse(new ApiResponse<>(false, "", "La cc ya esxiste"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User emailDuplicado = users.stream().filter(item -> item.getEmail().equals(user.getEmail())).findAny()
                .orElse(null);

        if (emailDuplicado != null) {
            response = setApiResponse(new ApiResponse<>(false, "", "El email ya existe"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        users.add(user);
        response = setApiResponse(new ApiResponse<>(true, users, "Usuario guardado"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<?> usersActivated() {
        Map<String, Object> response = new HashMap<>();

        List<User> usersActivate = users.stream().filter(item -> item.getIsActivated().equals(true))
                .collect(Collectors.toList());

        response = setApiResponse(new ApiResponse<>(true, usersActivate, "Lista de usuarios activos"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> userId(@RequestBody User user, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (userEncontrado == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro el usuario"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, userEncontrado, "Usuario encontrado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/allusers")
    public ResponseEntity<?> listarUsuarios() {
        Map<String, Object> response = new HashMap<>();

        response = setApiResponse(new ApiResponse<>(true, users, "Esta es la lista de usuarios"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> putUser(@RequestBody User user, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (userEncontrado == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "Usuario no encontrado"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (user.getCc().equals(null) || user.getCc().isEmpty() || user.getEmail().equals(null)
                || user.getEmail().isEmpty() || user.getLastname().equals(null) || user.getLastname().isEmpty()
                || user.getName().equals(null) || user.getName().isEmpty()) {
            response = setApiResponse(new ApiResponse<>(false, "", "Los campos son obligatorios para actualizar"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!userEncontrado.getCc().equals(user.getCc())) {
            User ccDuplicado = users.stream()
                    .filter(p -> p.getCc().equals(user.getCc())).findAny().orElse(null);

            if (ccDuplicado != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "La cc ya existe"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!userEncontrado.getEmail().equals(user.getEmail())) {
            User emailDuplicado = users.stream()
                    .filter(p -> p.getCc().equals(user.getEmail())).findAny().orElse(null);

            if (emailDuplicado != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "El email ya existe"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        userEncontrado.setCc(user.getCc());
        userEncontrado.setEmail(user.getEmail());
        userEncontrado.setLastname(user.getLastname());
        userEncontrado.setName(user.getName());

        response = setApiResponse(new ApiResponse<>(true, "", "Usuario actualizado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (userEncontrado == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "Usuario no encontrado"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        userEncontrado.setIsActivated(false);
        response = setApiResponse(new ApiResponse<>(true, "", "Usuario desactivado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /// ACCOUNT ///

    @PostMapping("/accounts")
    public ResponseEntity<?> postAccount(@RequestBody Account cuenta) {
        Map<String, Object> response = new HashMap<>();

        if (cuenta.getBalance() == 0 || cuenta.getNumber().equals(null) || cuenta.getNumber().isEmpty()
                || cuenta.getType().equals(null) || cuenta.getType().isEmpty() || cuenta.getIdUser().equals(null)) {
            response = setApiResponse(new ApiResponse<>(false, "", "Todos los datos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Account numeroRepetido = accounts.stream().filter(item -> item.getNumber().equals(cuenta.getNumber())).findAny()
                .orElse(null);

        if (numeroRepetido != null) {
            response = setApiResponse(new ApiResponse<>(false, "", "El numero de cuenta ya existe"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!cuenta.getType().equals("ahorro") && !cuenta.getType().equals("corriente")) {
            response = setApiResponse(new ApiResponse<>(false, "", "Solo se puede ahorro o corriente"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        accounts.add(cuenta);
        response = setApiResponse(new ApiResponse<>(false, accounts, "Cuenta bancaria creada"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/activas")
    public ResponseEntity<?> listarCuentas() {
        Map<String, Object> response = new HashMap<>();

        Account cuentasActivadas = accounts.stream().filter(item -> item.getIsActivated().equals(true)).findAny()
                .orElse(null);

        if (cuentasActivadas == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontraron cuentas activas"));
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
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la cuenta"));
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
    public ResponseEntity<?> putCuenta(@PathVariable UUID id, @RequestBody Account cuenta) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEcnontrada = accounts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cuentaEcnontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la cuenta"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (cuenta.getBalance() == 0 || cuenta.getNumber().equals(null) || cuenta.getNumber().isEmpty()
                || cuenta.getType().equals(null) || cuenta.getType().isEmpty()) {
            response = setApiResponse(new ApiResponse<>(false, "", "Todos los datos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!cuentaEcnontrada.getNumber().equals(cuenta.getNumber())) {
            Account cambioNumber = accounts.stream()
                    .filter(item -> item.getNumber().equals(cuenta.getNumber())).findAny().orElse(null);

            if (cambioNumber != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "No se puede cambiar el numero de cuenta"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!cuentaEcnontrada.getIdUser().equals(cuenta.getIdUser())) {
            Account cambioIdUser = accounts.stream()
                    .filter(item -> item.getIdUser().equals(cuenta.getIdUser())).findAny().orElse(null);

            if (cambioIdUser != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "El id de usuario es diferente"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!cuenta.getType().equals("ahorro") && !cuenta.getType().equals("corriente")) {
            response = setApiResponse(new ApiResponse<>(false, "", "Solo se puede ahorro o corriente"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        cuentaEcnontrada.setBalance(cuenta.getBalance());
        cuentaEcnontrada.setNumber(cuenta.getNumber());
        cuentaEcnontrada.setType(cuenta.getType());

        response = setApiResponse(new ApiResponse<>(false, "", "Cuenta actualizada"));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> desactivarCuenta(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEncontrada = accounts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cuentaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la cuenta bancaria"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        cuentaEncontrada.setIsActivated(false);
        response = setApiResponse(new ApiResponse<>(true, "", "Cuenta desactivada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
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
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la transferencia"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }

        response = setApiResponse(new ApiResponse<>(true, transferenciaEncontrada, "Transferencia encontrada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
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
            response = setApiResponse(new ApiResponse<>(false, "", "Cdt no encontrado"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, cdtEncontrado, "Cdt encontrado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cdt")
    public ResponseEntity<?> postCdt(@RequestBody Cdt cdt) {
        Map<String, Object> response = new HashMap<>();

        if (cdt.getPlazoMeses() == 0 || cdt.getIdUser().equals(null) || cdt.getMonto() == 0) {
            response = setApiResponse(new ApiResponse<>(false, "", "Todos los campos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (cdt.getPlazoMeses() != 3 && cdt.getPlazoMeses() != 6 && cdt.getPlazoMeses() != 9
                && cdt.getPlazoMeses() != 12) {
            response = setApiResponse(new ApiResponse<>(false, "", "Solo se pueden plazos de 3, 6, 9 y 12 meses"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        cdts.add(cdt);
        response = setApiResponse(new ApiResponse<>(true, cdts, "Cdt guardado"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/cdt/{id}")
    public ResponseEntity<?> putCdt(@PathVariable UUID id, @RequestBody Cdt cdt) {
        Map<String, Object> response = new HashMap<>();

        Cdt cdtEncontrado = cdts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cdtEncontrado == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "Cdt no encontrado"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (cdt.getPlazoMeses() == 0 || cdt.getIdUser().equals(null) || cdt.getMonto() == 0) {
            response = setApiResponse(new ApiResponse<>(false, "", "Los campos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!cdtEncontrado.getIdUser().equals(cdt.getIdUser())) {
            Cdt cambioIdUser = cdts.stream()
                    .filter(item -> item.getIdUser().equals(cdt.getIdUser())).findAny().orElse(null);

            if (cambioIdUser != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "No se puede cambiar el id del usuario"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        cdtEncontrado.setPlazoMeses(cdt.getPlazoMeses());
        cdtEncontrado.setIdUser(cdt.getIdUser());
        cdtEncontrado.setMonto(cdt.getMonto());

        response = setApiResponse(new ApiResponse<>(true, "", "Cdt actualizado"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cdt/{id}")
    public ResponseEntity<?> deleteCdt() {
        Map<String, Object> response = new HashMap<>();

        response = setApiResponse(
                new ApiResponse<>(false, "", "No se puede eliminar el cdt por la politica de privacidad"));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la tarjeta de credito"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response = setApiResponse(new ApiResponse<>(true, tarejtaEncontrada, "Tarjeta encontrada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tarjetaCredito")
    public ResponseEntity<?> postTarjetaCredito(@RequestBody TarjetaCredito tarjeta) {
        Map<String, Object> response = new HashMap<>();

        if (tarjeta.getNombre().equals(null) || tarjeta.getNombre().isEmpty() || tarjeta.getApellido().equals(null)
                || tarjeta.getApellido().isEmpty() || tarjeta.getEdad() == 0 || tarjeta.getIdUser().equals(null)) {
            response = setApiResponse(new ApiResponse<>(false, "", "Los datos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        tarjetasCreditos.add(tarjeta);
        response = setApiResponse(new ApiResponse<>(true, tarjetasCreditos, "Tarjeta de creadito agregada"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/tarjetaCredito/{id}")
    public ResponseEntity<?> putTarjeta(@RequestBody TarjetaCredito tarjeta, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        TarjetaCredito tarejtaEncontrada = tarjetasCreditos.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElse(null);

        if (tarejtaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la tarjeta de credito"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (tarjeta.getNombre().equals(null) || tarjeta.getNombre().isEmpty() || tarjeta.getApellido().equals(null)
                || tarjeta.getApellido().isEmpty() || tarjeta.getEdad() == 0 || tarjeta.getIdUser().equals(null)) {
            response = setApiResponse(new ApiResponse<>(false, "", "Los datos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!tarejtaEncontrada.getIdUser().equals(tarejtaEncontrada.getIdUser())) {
            TarjetaCredito cambioTarjeta = tarjetasCreditos.stream()
                    .filter(item -> item.getIdUser().equals(tarejtaEncontrada.getIdUser())).findAny().orElse(null);

            if (cambioTarjeta != null) {
                response = setApiResponse(new ApiResponse<>(false, "", "No se puede cambiar el id del usuario"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        tarejtaEncontrada.setApellido(tarjeta.getApellido());
        tarejtaEncontrada.setNombre(tarjeta.getNombre());
        tarejtaEncontrada.setEdad(tarjeta.getEdad());

        response = setApiResponse(new ApiResponse<>(true, "", "Tarjeta de credito actualizada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/tarjetaCredito/{id}")
    public ResponseEntity<?> deleteTarjeta(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        TarjetaCredito tarejtaEncontrada = tarjetasCreditos.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElse(null);

        if (tarejtaEncontrada == null) {
            response = setApiResponse(new ApiResponse<>(false, "", "No se encontro la tarjeta de credito"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        tarjetasCreditos.remove(tarejtaEncontrada);
        response = setApiResponse(new ApiResponse<>(true, "", "Tarjeta de credito eliminada"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /// SIMULAR CDT ///

    @PostMapping("/cdt/simular")
    public ResponseEntity<?> simularCdt(@RequestBody Cdt cdt) {
        Map<String, Object> response = new HashMap<>();

        String message = "";
        double meses3 = (cdt.getMonto() * 0.63) / 4;
        double meses6 = (cdt.getMonto() * 0.85) / 4;
        double meses9 = (cdt.getMonto() * 0.113) / 4;
        double meses12 = (cdt.getMonto() * 0.149) / 4;

        if (cdt.getPlazoMeses() == 0 || cdt.getIdUser().equals(null) || cdt.getMonto() == 0) {
            response = setApiResponse(new ApiResponse<>(false, "", "Todos los campos son obligatorios"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (cdt.getPlazoMeses() != 3 && cdt.getPlazoMeses() != 6 && cdt.getPlazoMeses() != 9
                && cdt.getPlazoMeses() != 12) {
            response = setApiResponse(new ApiResponse<>(false, "", "Solo se pueden plazos de , 6, 9 y 12 meses"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (cdt.getPlazoMeses() == 3) {
            message = "La tasa de interes anual a 3 meses es de " + meses3 / 0.4;
        }

        if (cdt.getPlazoMeses() == 6) {
            message = "La tasa de interes anual a 6 meses es de " + meses6 / 4;
        }

        if (cdt.getPlazoMeses() == 9) {
            message = "La tasa de interes anual a 9 meses es de " + meses9 / 4;
        }

        if (cdt.getPlazoMeses() == 12) {
            message = "La tasa de interes anual a 12 meses es de " + meses12 / 4;
        }

        response = setApiResponse(new ApiResponse<>(true, "", message));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

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

    // TRANSFERENCIAS ///

    @PostMapping("/transfers")
    public ResponseEntity<?> transferMoney(@RequestBody Transfer transfer) {
        Map<String, Object> response = new HashMap<>();

        Account originAccount = accounts.stream().filter(item -> item.getId().equals(transfer.getOriginAccount()))
                .findAny().orElse(null);

        if (originAccount == null || originAccount.getIsActivated() == false) {
            response = setApiResponse(new ApiResponse<>(false, "", "Cuenta bancaria de origen no encontrada"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Account destinationAccount = accounts.stream()
                .filter(item -> item.getId().equals(transfer.getDestinationAccount())).findAny().orElse(null);

        if (destinationAccount == null || destinationAccount.getIsActivated() == false) {
            response = setApiResponse(new ApiResponse<>(false, "", "Cuenta bancaria de destino no encontrada"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (originAccount.getBalance() < transfer.getAmount()) {
            response = setApiResponse(
                    new ApiResponse<>(false, "", "No tiene saldo sufienciente para realizar la transferencia"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        originAccount.setBalance(originAccount.getBalance() - transfer.getAmount());

        destinationAccount.setBalance(destinationAccount.getBalance() + transfer.getAmount());

        transfers.add(transfer);
        response = setApiResponse(new ApiResponse<>(true, transfer, "Transferencia bancaria realizada con éxito"));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }



    

}