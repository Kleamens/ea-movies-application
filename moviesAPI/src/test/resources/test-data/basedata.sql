INSERT INTO director (id, name,  net_worth) VALUES ('d639c6ea-ee0a-47d8-80ae-398152a2644a', 'John Doe',3.4);
INSERT INTO director (id, name,  net_worth) VALUES ('fa4bfeee-0c1c-4f52-b5b0-04b616f6e528', 'Joe Rogan',1.2);

INSERT INTO movie
    (id,title,release_year,last_modified,overview
    ,meta_score,certificate,runtime,genre,imdb_rating,revenue,director_id)
VALUES ('07ab22d5-67c7-45ec-953a-17ca0e1a8bd6','Kill me','1994-01-01'
       ,'2024-05-20 13:23:59.375119','idk',70,'A',120,'Comedy,Drama'
    ,9.3,810902,'fa4bfeee-0c1c-4f52-b5b0-04b616f6e528');


INSERT INTO movie
(id,title,release_year,last_modified,overview
,meta_score,certificate,runtime,genre,imdb_rating,revenue,director_id)
VALUES ('3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7','Bruh','1998-01-01',
        '2024-05-20 13:23:59.37519','no idea',90,'U',80,'Comedy',
        4.2,5762,'fa4bfeee-0c1c-4f52-b5b0-04b616f6e528');

INSERT INTO public.rental_user (id, name,  surname,email) VALUES ('ce69a41b-da7d-4f65-97ff-f5bff832c783', 'Joe', 'Sunderland','joesunderland@gmail.com');
