INSERT INTO employee
SELECT generate_series(4, 7),
       substr(gen_random_uuid()::text, 1, 7), /*name*/
       substr(gen_random_uuid()::text, 1, 8),/*surname*/
    /*каждый раз рандомно выбирает из предоставленного списка
      случайное значение*/
       (array [1, 2, 3])[floor(random() * 3 + 1)],/*address_id*/
       (array [1, 2, 3])[floor(random() * 3 + 1)],/*department_id*/
(select regexp_replace(
               rpad(CAST (random() AS text),12,
                    CAST(random() AS text)),
               '^0\.(\d{3})(\d{3})(\d{4}).*$','7\1\2\3') AS random);
/*последняя строка будет генерировать один и тот же номер для всех строк.*/