# PAP21L-Z13

Aplikacja webowa do reklamowania i rezerwacji noclegów. 

Instalacja:
- Sklonować z repozytorium projekt zawierający backend aplikacji. Projekt wykonano w javie w wersji 11, więc zaleca się wykorzystanie tej wersji podczas testowania, i uruchamiania
- Należy utworzyć bazę danych dla aplikacji. Konfiguracja w pliku application.properties odpowiada bazie danych Oracle. Dla tej bazy zainstalowano driver.    
- W utworzonej bazie danych uruchomić skrypt Travel_NOW_DB_CREATE.sql załączony do repozytorium w folderze DB, który utworzy wszystkie tablice i inne obiekty bazy danych,
- W utworzonej bazie danych uruchomić plik skrypt Travel_NOW_DB_INSERT.sql załączony do repozytorium w folderze DB, który utworzy przykładowe dane testowe.
- Sklonować z repozytorium projekt forntendu,
- Upewnić się, że na lokalnej maszynie zainstalowany jest Node.js oraz menager pakietów npm. (Ubuntu: sudo apt install nodejs, sudo apt install npm; Windows: oprogramowanie należy pobrać z https://nodejs.org/en/download/)
- Otworzyć projekt backendu w visual studio code lub dowolnym innym IDE do programowania w javie oraz uruchomić aplikację (“F5” w vs), upewnić się, że aplikacja pracuje na porcie 8080, jeśli nie należy dokonać zmiany w konfiguracji proxy w aplikacji React (plik package.json w katalogu głównym) na port, na którym pracuje aplikacja serwera,
- Otworzyć katalog główny projektu React oraz uruchomić aplikację, wpisując w wiersz poleceń npm start, aplikacja będzie dostępna na porcie 3000. Można ją obsługiwać w dowolnej przeglądarce internetowej pod adresem localhost:3000/

Dokumentacja:
- dokładna dokumentacja projektu znajduje się w katalogu Documents
