### Лабораторная 8.

_Цель_: получить практический опыт применения паттерна `SettableClock` при реализации тестов.

Необходимо реализовать интерфейс `statistic.EventsStatistic`, который считает события, происходящие в системе. 

Реализация должна хранить статистику ровно за последний час и подсчитывать, сколько событий каждого типа произошло в минуту.

Интерфейс `statistic.EventsStatistic`:
```java
public interface statistic.EventsStatistic {    
  void incEvent(String name);    
  ... getEventStatisticByName(String name);
  ... getAllEventStatistic();
  void printStatistic();
}
```
-	`incEvent(String name)` - инкрементит число событий `name`;
-	`getEventStatisticByName(String name)` - выдает `rpm` (`request per minute`) события name за последний час;
-	`getAllEventStatistic()` - выдает `rpm` всех произошедших событий за прошедший час;
-	`printStatistic()` - выводит в консоль `rpm` всех произошедших событий;

Реализацию `statistic.EventsStatistic` необходимо покрыть тестами, используя паттерн `SettableClock`, рассмотренный на лекции.

Тесты не должны использовать `sleep`'ы и должны выполняться быстро.

#### Тестирование
- `mvn test`