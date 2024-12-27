## Opis aplikacji
Aplikacja jest prostą implementacją REST API do zarządzania produktami. Umożliwia wykonywanie podstawowych operacji CRUD (Create, Read, Update, Delete) na produktach.

Aplikacja wykorzystuje **Spring Boot**, **Spring Data JPA** i **H2 Database**.

## Struktura projektu
- **ProductController** - kontroler obsługujący żądania HTTP.
- **ProductService** - logika biznesowa aplikacji.
- **ProductRepository** - repozytorium dostępu do bazy danych.
- **Product** - encja reprezentująca produkt.
- **ProductRequest** i **UpdateProductRequest** - klasy DTO do przyjmowania danych wejściowych.
- **ProductResponse** - klasa DTO do zwracania odpowiedzi.
- **ProductMapper** - komponent mapujący dane między obiektami DTO a encją.
- **ProductExceptionAdvisor** - globalny handler wyjątków związanych z produktami.
- **ProductNotFoundException** - wyjątek dla nieznalezionych produktów.
- **ProductExceptionSupplier** - dostarcza wyjątki w formie Supplier.
- **FirstRestApiApplication** - główna klasa startowa aplikacji.

## Endpointy
### 1. Tworzenie nowego produktu
- **Endpoint**: `POST /api/v1/products`
- **Body**:
  ```json
  {
    "name": "Product Name"
  }
  ```
- **Odpowiedź**:
  ```json
  {
    "id": 1,
    "name": "Product Name"
  }
  ```

### 2. Pobieranie produktu po ID
- **Endpoint**: `GET /api/v1/products/{id}`
- **Przykład odpowiedzi**:
  ```json
  {
    "id": 1,
    "name": "Product Name"
  }
  ```

### 3. Pobieranie wszystkich produktów
- **Endpoint**: `GET /api/v1/products`
- **Przykład odpowiedzi**:
  ```json
  [
    {
      "id": 1,
      "name": "Product Name"
    },
    {
      "id": 2,
      "name": "Another Product"
    }
  ]
  ```

### 4. Aktualizacja produktu
- **Endpoint**: `PUT /api/v1/products/{id}`
- **Body**:
  ```json
  {
    "name": "Updated Product Name",
    "id": 1
  }
  ```
- **Odpowiedź**:
  ```json
  {
    "id": 1,
    "name": "Updated Product Name"
  }
  ```

### 5. Usuwanie produktu
- **Endpoint**: `DELETE /api/v1/products/{id}`
- **Odpowiedź**: `204 No Content`

## Przepływ danych w aplikacji
1. **Żądanie** trafia do kontrolera (`ProductController`), który odpowiada za obsługę żądań REST API.
2. Kontroler deleguje logikę biznesową do **serwisu** (`ProductService`).
3. Serwis korzysta z **repozytorium** (`ProductRepository`) w celu dostępu do bazy danych.
4. Dane są mapowane między obiektami domenowymi (`Product`) i obiektami DTO (`ProductRequest`, `UpdateProductRequest`, `ProductResponse`) za pomocą odpowiedniego mappera (`ProductMapper`).
5. W przypadku nieznalezienia produktu rzucany jest wyjątek `ProductNotFoundException`, który jest obsługiwany przez `ProductExceptionAdvisor`.
6. Dostarczanie wyjątków jest wspierane przez klasę `ProductExceptionSupplier`.
7. Odpowiedź jest zwracana do użytkownika.


## Wyjątki
- **ProductNotFoundException**: Rzucany, gdy produkt o podanym ID nie istnieje w bazie danych.
- **ProductExceptionAdvisor**: Globalny handler do obsługi wyjątków związanych z produktami, zwracający komunikaty błędów w formacie JSON.

## Opis metod i adnotacji

### Główne adnotacje Spring Boot:
- `@RestController` - Oznacza klasę jako kontroler REST, który zwraca dane w formacie JSON.
- `@RequestMapping` - Ustawia ścieżkę bazową dla wszystkich endpointów w kontrolerze.
- `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping` - Odpowiadają za mapowanie odpowiednich żądań HTTP (POST, GET, PUT, DELETE).
- `@RequestBody` - Służy do pobrania danych wejściowych z ciała żądania HTTP.
- `@PathVariable` - Służy do pobierania wartości ze ścieżki URL.
- `@ResponseStatus` - Ustawia status odpowiedzi HTTP.
- `@ExceptionHandler` - Obsługuje wyjątki na poziomie globalnym.
- `@Service` - Oznacza klasę jako komponent logiki biznesowej aplikacji.
- `@Repository` - Oznacza klasę jako komponent dostępu do danych (DAO).
- `@Component` - Uniwersalna adnotacja oznaczająca klasę zarządzaną przez Spring.
- `@SpringBootApplication` - Punkt wejściowy aplikacji Spring Boot.

### Metody w projekcie:

1. **`ProductController`**
    - `create()` - Tworzy nowy produkt.
    - `find()` - Zwraca produkt o podanym ID.
    - `findAll()` - Zwraca wszystkie produkty.
    - `update()` - Aktualizuje istniejący produkt.
    - `delete()` - Usuwa produkt o podanym ID.

2. **`ProductService`**
    - `create()` - Wywołuje `ProductMapper` do przekształcenia DTO na encję i zapisuje produkt.
    - `find()` - Wyszukuje produkt po ID, obsługując wyjątki za pomocą `ProductExceptionSupplier`.
    - `findAll()` - Pobiera wszystkie produkty z bazy danych.
    - `update()` - Aktualizuje dane istniejącego produktu.
    - `delete()` - Usuwa produkt z bazy danych.

3. **`ProductMapper`**
    - `toProduct()` - Mapuje `ProductRequest` na encję `Product`.
    - `toProduct()` - Aktualizuje encję `Product` na podstawie `UpdateProductRequest`.
    - `toProductResponse()` - Mapuje encję `Product` na `ProductResponse`.

4. **`ProductExceptionAdvisor`**
    - `productNotFound()` - Obsługuje wyjątek `ProductNotFoundException`, zwracając komunikat błędu.

5. **`ProductRepository`**
    - Wykorzystuje interfejs `JpaRepository` do operacji na bazie danych.

### Dlaczego ProductRepository działa bez implementacji metod?
Interfejs **`ProductRepository`** jest pusty, ale korzysta z metod takich jak `save`, `findById`, `findAll` i `deleteById`, ponieważ rozszerza on interfejs **`JpaRepository`** z Spring Data JPA.

**Spring Data JPA** automatycznie dostarcza implementację interfejsów, które dziedziczą po `JpaRepository`. Dzieje się tak dzięki **mechanizmowi refleksji i proxy** w Spring Framework. Kluczowe jest tutaj to, że:

1. **JpaRepository** - jest predefiniowanym interfejsem w Spring Data JPA, który zawiera domyślne metody operacji na bazie danych (CRUD, paginacja, sortowanie itp.).
2. **ProductRepository** dziedziczy te metody bez konieczności implementacji. Spring Boot, przy starcie aplikacji, **automatycznie generuje klasę proxy**, która implementuje `ProductRepository` i dostarcza kod dla metod `save`, `findById`, `findAll`, `deleteById` itp.
3. Dzięki **Spring Context** i **Dependency Injection**, gdy `ProductRepository` jest używany w `ProductService`, Spring automatycznie podkłada odpowiednią implementację tego interfejsu.
