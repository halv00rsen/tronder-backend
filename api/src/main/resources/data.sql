-- noinspection SqlNoDataSourceInspectionForFile

insert into dialect (`id`, `description`, `display_name`, `created_by`, `public_dialect`) values
(1, 'Snakket i Trøndelag', 'Trøndersk', 'another-long-sub', 1),
(2, 'Østlending', 'Østlandsk', 'long-sub', 0);

insert into word_entity (`id`, `translation`, `description`, `word_text`) values
(1, 'oversetting', 'en beskrivelse', 'et ord'),
(2, 'oversetting2', 'enda en beskrivelse', 'et annet ord'),
(3, 'oversetting3', 'en beskrivelse til', 'et ord annen dialect');

insert into dialect_words (`dialect_id`, `words_id`) values
(1, 1),
(1, 2),
(2, 3);
