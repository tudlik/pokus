Info k projektu:

- swagger je dostupný na adrese http://localhost:8082/swagger-ui/index.html

- požadavek se musí poslat v tomto tvaru localhost:8082/projects?restrict=RESTRICT_ALL&limit=10&sorting=SORTING_ID&ordering=ORDERING_ASC
    - restrict - může mít hodnoty RESTRICT_ALL, RESTRICT_EVEN, RESTRICT_ODD - filtruje id záznamu na liché, sudé nebo nechá všechny záznamy - by default RESTRICT_ALL
    - sorting - určí podle jakého atributu se bude seznam seřazen - povolené hodnoty jsou SORTING_ID nebo SORTING_NAME - by default je SORTING_ID
    - ordering - nastavení jestli se bude atribut řadit sestupně nebo vzestupně - povolené hodnoty ORDERING_ASC nebo ORDERING_DESC - by default je ORDERING_ASC
    - limit - musí být kladné celé číslo vyšší než 1 - počet záznamů, které se vrátí zpět - by default je 10