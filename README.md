Info k projektu:

- swagger je dostupný na adrese http://localhost:8082/swagger-ui/index.html
- pozadavek musi byt v tomto tvaru localhost:8082/projects?restrict=RESTRICT_ALL&limit=10&sorting=SORTING_ID&ordering=ORDERING_ASC
    - restrict může mít hodnoty RESTRICT_ALL,.... - filtruje id záznamu na liché, sudé nebo nechá všechny - by default všechny
    - sorting... určí podle jakého atributu se bude seznam seřazen - by default je id
    - ordering .... nastavení jestli se bude atribut řadit sestupně nebo vzestupně - by default asc
    - limit.... počet záznamů, které se vrátí zpět - by default je 10