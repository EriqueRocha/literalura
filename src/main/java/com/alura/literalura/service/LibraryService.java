package com.alura.literalura.service;

import com.alura.literalura.gutendex.Response;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookEntity;
import com.alura.literalura.model.Person;
import com.alura.literalura.model.PersonEntity;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.repository.PersonRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Component
public class LibraryService implements ApplicationRunner {

    private static final String BASE_URL = "https://gutendex.com/books?search=";

    private final BookRepository bookRepository;

    private final PersonRepository personRepository;

    public LibraryService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Escolha uma das opções:");
            System.out.println("1 - Buscar livro pelo título/autor");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um determinado ano");
            System.out.println("5 - Listar livros em determinado idioma");
            System.out.println("6 - Listar top livros mais baixados");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEmAno();
                    break;
                case 5:
                    listarLivrosEmIdioma();
                    break;
                case 6:
                    listarTop();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    public void buscarLivroPeloTitulo() {

        System.out.println("Digite o nome do livro ou do autor: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        try {
            List<Book> books = searchBooks(query);
            if (books.size() < 1){
                System.out.println("=========================================================");
                System.out.println("|| Não foi encontrado nenhum livro por titulo ou autor ||");
                System.out.println("=========================================================");
            }
            List<String> bookNames = new ArrayList<>();
            for (Book book : books) {
                bookNames.add(book.getTitle());
                BookEntity bookEntity = new BookEntity();
                BeanUtils.copyProperties(book, bookEntity);
                List<Person> persons = book.getAuthors();
                List<PersonEntity> personEntitys = new ArrayList<>();
                List<String> authors = new ArrayList<>();

                for (Person person: persons){
                    PersonEntity personEntity = new PersonEntity();
                    personEntity.setName(person.getName());
                    personEntity.setBirth_year(person.getBirth_year());
                    personEntity.setDeath_year(person.getDeath_year());
                    personEntity.setBooks(bookNames);
                    authors.add(person.getName());
                    if (!personRepository.existsByName(person.getName())){
                        personEntitys.add(personEntity);
                        personRepository.save(personEntity);
                    }else {
                        personEntity = personRepository.findByName(person.getName());
                    }
                }

                bookEntity.setAuthors(personEntitys);

                String model =
                        "=========================================\n"+
                        "Titulo: "+book.getTitle()+"\n" +
                        "Autores: "+adjustList(authors)+"\n" +
                        "Linguas: "+adjustList(book.getLanguages())+"\n" +
                        "Quantidade de downloads: "+book.getDownload_count()+"\n"+
                        "=========================================\n";

                if (!bookRepository.existsByTitle(book.getTitle())){
                    bookRepository.save(bookEntity);
                }
                System.out.println(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarLivrosRegistrados() {
        List<BookEntity> books = bookRepository.findAll();
        List<PersonEntity> personEntities = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        for (BookEntity book : books) {
            personEntities.addAll(book.getAuthors());
            for (PersonEntity person: personEntities){
                authors.add(person.getName());

            }
            System.out.println(
                    "=================================\n"+
                    "Título: " + book.getTitle() +"\n"+
                    "Autores: " + adjustList(authors) +"\n"+
                    "Linguagens: " + adjustList(book.getLanguages())+"\n"+
                    "Download Count: " + book.getDownload_count()+"\n"+
                    "=================================\n"
            );
        }
    }

    public void listarAutoresRegistrados() {
        List<PersonEntity> personEntities = personRepository.findAll();
        for (PersonEntity person : personEntities) {
            System.out.println(
                    "=================================\n"+
                    "Nome: " + person.getName() +"\n"+
                    "Ano de nascimento: " + person.getBirth_year() +"\n"+
                    "Ano da Morte: " + person.getDeath_year()+"\n"+
                    "Livros: " + adjustList(person.getBooks())+"\n"+
                    "=================================\n");
        }
    }

    public void listarAutoresVivosEmAno() {
        System.out.println("Digite o ano: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();

        if (query.length() == 4 && query.matches("\\d{4}")) {
            List<PersonEntity> personEntities = personRepository.findByYearInRange(Integer.valueOf(query));

            if (personEntities.size()<1){
                System.out.println("=========================================================");
                System.out.println("|| Nenhum autor da base de dados estava vivo neste ano ||");
                System.out.println("=========================================================");
            }
            for (PersonEntity person : personEntities) {
                System.out.println(
                        "=================================\n"+
                                "Nome: " + person.getName() +"\n"+
                                "Ano de nascimento: " + person.getBirth_year() +"\n"+
                                "Ano da Morte: " + person.getDeath_year()+"\n"+
                                "Livros: " + adjustList(person.getBooks())+"\n"+
                                "=================================\n");
            }
        } else {
            System.out.println("Ano inválido. Tente novamente.");
        }
    }

    public void listarLivrosEmIdioma() {
        List<String> validLanguages = List.of("es", "en", "fr", "pt");
        System.out.println("qual o idioma desejado?: ");
        System.out.println("es - espanhol");
        System.out.println("en - inglês");
        System.out.println("fr - francês");
        System.out.println("pt - português");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        if (validLanguages.contains(query)) {
            List<BookEntity> books = bookRepository.findByLanguagesIn(Collections.singletonList(query));
            List<PersonEntity> personEntities = new ArrayList<>();
            List<String> authors = new ArrayList<>();
            for (BookEntity book : books) {
                personEntities.addAll(book.getAuthors());
                for (PersonEntity person: personEntities){
                    authors.add(person.getName());
                }
                System.out.println(
                        "=================================\n"+
                                "Título: " + book.getTitle() +"\n"+
                                "Autores: " + adjustList(authors) +"\n"+
                                "Linguagens: " + adjustList(book.getLanguages())+"\n"+
                                "Download Count: " + book.getDownload_count()+"\n"+
                                "=================================\n"
                );
            }

        } else {
            System.out.println("==========================================");
            System.out.println("|| Linguagem inválida. Tente novamente. ||");
            System.out.println("==========================================");
        }

    }

    public void listarTop() {
        List<BookEntity> books = bookRepository.findTopByDownloadCount();
        if (books.size() > 0){
            List<PersonEntity> personEntities = new ArrayList<>();
            List<String> authors = new ArrayList<>();
            int count = 0;
            for (BookEntity book : books) {
                count ++;
                personEntities.addAll(book.getAuthors());
                for (PersonEntity person: personEntities){
                    authors.add(person.getName());
                }
                System.out.println(
                        "=================================\n"+
                                "----- TOP "+count+" -----\n"+
                                "Título: " + book.getTitle() +"\n"+
                                "Autores: " + adjustList(authors) +"\n"+
                                "Linguagens: " + adjustList(book.getLanguages())+"\n"+
                                "Download Count: " + book.getDownload_count()+"\n"+
                                "=================================\n"
                );
            }
        }else {
            System.out.println("==================================================");
            System.out.println("|| Ainda não tem livros salvos na base de dados ||");
            System.out.println("==================================================");
        }
    }

    public static List<Book> searchBooks(String query) throws Exception {
        String urlString = BASE_URL + query.replace(" ", "%20");
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            Gson gson = new Gson();
            Type responseType = new TypeToken<Response>(){}.getType();
            Response apiResponse = gson.fromJson(responseBody, responseType);
            return apiResponse.getResults();
        } else {
            throw new Exception("GET request not worked, status code: " + response.statusCode());
        }
    }

    public static String adjustList(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (String elemento : lista) {
            joiner.add(elemento);
        }

        return joiner.toString();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        exibirMenu();
    }
}
