package com.crapz.literatura.main;

import com.crapz.literatura.model.*;
import com.crapz.literatura.repository.AutorRepository;
import com.crapz.literatura.repository.LibroRepository;
import com.crapz.literatura.service.ConsumoApi;
import com.crapz.literatura.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private Scanner entrada = new Scanner(System.in);
    private String urlBase = System.getenv("URL_BASE");
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    =====================================
                    Elija la opción a través de su número
                    =====================================
                    1- Buscar libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    
                    0- Salir                    
                    =====================================
                    """;

            System.out.println(menu);
            System.out.print("Opción a elegir: ");
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var nombreTitulo = entrada.nextLine();
        var json = consumoApi.obtenerDatos(urlBase + "?search=" + nombreTitulo.replace(" ", "%20"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda != null && !datosBusqueda.resultados().isEmpty()) {
            return datosBusqueda.resultados().get(0);
        } else {
            System.out.println("Libro no encontrado.");
            return null;
        }
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        if (datos != null) {
            Optional<Libro> libroExistente = libroRepository.findByTituloContainingIgnoreCase(datos.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("No se puede registrar el mismo libro más de una vez.");
                return;
            }

            DatosAutor datosAutor = datos.autor().get(0);
            Optional<Autor> autorRegistrado = autorRepository.findByNombreContainingIgnoreCase(datosAutor.nombre());

            Autor autor;
            if (autorRegistrado.isPresent()) {
                autor = autorRegistrado.get();
            } else {
                Autor nuevoAutor = new Autor(datosAutor);
                autor = autorRepository.save(nuevoAutor);
            }

            Libro libro = new Libro(datos);
            libro.setAutor(autor);

            try {
                libroRepository.save(libro);
                System.out.println("\n--- Libro Guardado con Éxito ---");
                System.out.println(libro);
            } catch (Exception e) {
                System.out.println("Error de integridad: " + e.getMessage());
            }
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(a -> {
                        System.out.println("\nAutor: " + a.getNombre());
                        System.out.println("Fecha de nacimiento: " + a.getFechaDeNacimiento());
                        System.out.println("Fecha de fallecimiento: " + a.getFechaDeFallecimiento());
                        List<String> titulosLibros = a.getLibros().stream()
                                .map(Libro::getTitulo)
                                .toList();
                        System.out.println("Libros: " + titulosLibros);
                    });
        }
    }

    private void listarAutoresVivosEnDeterminadoAno() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar: ");
        try {
            var ano = entrada.nextInt();
            entrada.nextLine();

            List<Autor> autores = autorRepository.autoresVivosEnDeterminadoAno(ano);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + ano);
            } else {
                autores.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, ingrese un número de año válido.");
            entrada.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
            Ingrese el idioma para buscar los libros:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """);
        var idioma = entrada.nextLine().toLowerCase();

        List<Libro> libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado (" + idioma + ").");
        } else {
            libros.forEach(System.out::println);
        }
    }
}
