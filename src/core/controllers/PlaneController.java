package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Plane;
import core.models.storage.PlaneStorage;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaneController {

    private final PlaneStorage storage = new PlaneStorage();
    private static final Pattern PLANE_ID_PATTERN = Pattern.compile("^[A-Z]{2}\\d{5}$");

    public PlaneController() {
    }

    public Response addPlane(String id,
            String brand,
            String model,
            int maxCapacity,
            String airline) {
        // Validar ID
        if (id == null || !PLANE_ID_PATTERN.matcher(id).matches()) {
            return new Response(
                    "El ID debe tener formato XXYYYYY (2 letras + 5 dígitos).",
                    Status.BAD_REQUEST
            );
        }
        if (storage.existsById(id)) {
            return new Response("Ya existe un avión con este ID.", Status.BAD_REQUEST);
        }

        // Validar campos no vacíos
        if (brand == null || brand.trim().isEmpty()) {
            return new Response("La marca no puede estar vacía.", Status.BAD_REQUEST);
        }
        if (model == null || model.trim().isEmpty()) {
            return new Response("El modelo no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (airline == null || airline.trim().isEmpty()) {
            return new Response("La aerolínea no puede estar vacía.", Status.BAD_REQUEST);
        }

        // Validar capacidad
        if (maxCapacity <= 0) {
            return new Response(
                    "La capacidad máxima debe ser mayor que 0.",
                    Status.BAD_REQUEST
            );
        }

        // Construir y persistir
        Plane plane = new Plane(id, brand, model, maxCapacity, airline);
        boolean saved = storage.addPlane(plane);
        if (!saved) {
            return new Response("No se pudo guardar el avión.", Status.INTERNAL_SERVER_ERROR);
        }

        // Devolver copia (Prototype)
        Plane copy = new Plane(
                plane.getId(),
                plane.getBrand(),
                plane.getModel(),
                plane.getMaxCapacity(),
                plane.getAirline()
        );
        return new Response("Avión creado exitosamente.", Status.CREATED, copy);
    }

    public List<Plane> getPlanes() {
        return storage.getAllPlanes().stream()
                .sorted(Comparator.comparing(Plane::getId))
                .map(p -> new Plane(
                p.getId(),
                p.getBrand(),
                p.getModel(),
                p.getMaxCapacity(),
                p.getAirline()
        ))
                .collect(Collectors.toList());
    }
}
