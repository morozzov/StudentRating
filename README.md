# StudentRating

**Студенческий рейтинг** - сервис для оценки учебной и внеучебной деятельности студентов преподавателями.

Студенты могут посмотреть свой рейтинг и рейтинг других студентов. Могут ознакомиться с личными заслугами и оплошностями своими и остальных студентов.

Преподаватели имеют возможность вносить достижения и проступки студентов, оценивая это баллами, которые прибавляются или вычитаются из имеющихся у данного студента баллов.

#

Клиентская часть представлена сайтом.

Разделы сайта:
- Главная страница со списком всех студентов, отсортированным по количеству баллов;
- Страница со списком поручений, за которые можно получить дополнительные баллы;
- Личная страница студента.

Данные хранятся в базе данных, состоящей из следующих таблиц:
- Students *- таблица с информацией о студениах;*
- Teachers *- таблица с информацией о преподавателях;*
- Tasks *- таблица с информацией о порученпиях и их статусом доступности;*
- Activity *- таблица с информацией о деятельности студентов, то есть их заслугах или проступках;*
- Logs *- таблица логов действий, влияющих на колличество баллов студентов.*

