# RBT_TEHNICKI_ZADATK
Sistem za upravljanje i praćenje pošiljki


Uputsvo za pokretanje:
1) klonirati projekat sa git-a
2) pozicionirati se u koren projekta
3) Pokrenite komandu: docker compose up --build
4) Swagger dokumentacija je dostupna na adresi: localhost:8080/api/swagger-ui.html


Primer sadrzaja csv fajla za import posiljski
(
username polje je obavezno
description polje nije obavezno,
state polje ocekuje jednu od vrednosti is skupa stanja posiljke (CREATED, IN_TRANSIT, DELIVERED, CANCELLED). Polje nije obavezno, ima podrazumevanu vrednost CREATED
):

username,description,state
nikola,porudzbina3,
nikola,,
ivan,porudzbina4,
ivan,porudzibna5,IN_TRANSIT
ivan,,DELIVERED
ivan,nepotpuna porudzbina,CANCELLED
