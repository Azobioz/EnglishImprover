call words_insert('Apple', 'Яблоко');
call words_insert('Car', 'Кошка');
call words_insert('Piano', 'Пианино');
call words_insert('Ear', 'Ухо');
call words_delete(1);
select * from words;