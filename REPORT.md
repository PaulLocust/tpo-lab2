# Лабораторная работа №2: интеграционное тестирование системы функций

## 1. Текст задания и система функций

Требовалось реализовать и протестировать систему функций:

- при $x \le 0$:

$$
\begin{aligned}
&((((((((((((((\cos(x) / \sin(x)) * \sin(x)) ^ 3) ^ 2) - \cot(x)) - \sec(x)) * \cos(x)) ^ 2) / (\csc(x) + (\sin(x) ^ 3))) * (\cot(x) / \cot(x))) \\
&\quad - \sin(x)) + ((\tan(x) / ((((\tan(x) - \csc(x)) + \cos(x)) ^ 2) ^ 3)) * (\sec(x) - \cot(x)))) \\
&\quad - (((\cos(x) * ((\csc(x) - \sin(x)) ^ 3)) / ((((\tan(x) ^ 3) * ((\cot(x) + \sin(x)) * \cos(x))) / \cos(x)) ^ 3)) \\
&\qquad * (\cot(x) * ((((\sin(x) * (\cos(x) ^ 3)) / \sin(x)) ^ 2) - \sin(x))))
\end{aligned}
$$

- при $x > 0$:

$$
(((((\log_3(x) + \ln(x)) / \log_2(x)) - (\ln(x) * \log_5(x))) ^ 2) - (\log_2(x) * \log_{10}(x)))
$$

Ограничения варианта:

- базовая тригонометрическая функция: `cos(x)` (разложение в ряд);
- `sin(x)` вычисляется через зависимость от `cos(x)`;
- базовая логарифмическая функция: `ln(x)` (разложение в ряд);
- остальные тригонометрические/логарифмические функции выражены через базовые;
- реализована возможность выгрузки любого модуля в CSV.

## 2. Реализация

Основные модули в проекте:

- `src/main/java/lab/pavel/function/trig/CosSeries.java` — `cos(x)` через ряд Тейлора;
- `src/main/java/lab/pavel/function/trig/SinFromCos.java` — `sin(x)` через зависимость от `cos`;
- `src/main/java/lab/pavel/function/trig/TanModule.java`, `CotModule.java`, `SecModule.java`, `CscModule.java` — производные тригонометрические функции;
- `src/main/java/lab/pavel/function/log/LnSeries.java` — `ln(x)` через ряд;
- `src/main/java/lab/pavel/function/log/LogBaseModule.java` — `log_b(x)` через `ln(x)`;
- `src/main/java/lab/pavel/function/system/SystemFunction.java` — итоговая кусочная система;
- `src/main/java/lab/pavel/io/CsvExporter.java` — выгрузка `X;Result` с произвольным шагом;
- `src/main/java/lab/pavel/Main.java` — CLI для экспорта:
  - `single <module> <from> <to> <step> <outputCsv> [delimiter]`
  - `all <from> <to> <step> <outputDir> [delimiter]`

## 3. Табличные заглушки и стратегия интеграции

Стратегия интеграции: **пошаговая (bottom-up) по одному модулю**.

Этапы:

1. Полностью табличная сборка системы на Mockito-стабах (`SystemFunctionWithTableStubsTest`).
2. Интеграция тригонометрической ветки (`SystemFunctionTrigIntegrationTest`):
   - `cos` реальный,
   - `sin` зависит от `cos`,
   - остальные триг-функции реальны,
   - логарифмы — заглушки.
3. Интеграция логарифмической ветки (`SystemFunctionLogIntegrationTest`):
   - `ln`, `log2`, `log3`, `log5`, `log10` реальные,
   - тригонометрия — заглушки.
4. Полная интеграция (`SystemFunctionFullIntegrationTest`) со всеми реальными модулями.

Табличные заглушки реализованы через Mockito (`when(mock.calculate(x)).thenReturn(value)`), значения взяты как табличные приближения по графикам/референсным точкам.

## 4. Тестовое покрытие и обоснование

Использован **JUnit 5** + **параметризованные тесты** + **Mockito**.

Покрыты следующие группы:

1. Базовые модули:
   - точность вычисления `cos`, `ln` на реперных точках;
   - периодичность `cos`;
   - область допустимых значений `ln`.
2. Производные модули:
   - корректность `tan/cot/sec/csc`;
   - асимптоты (`cos=0` для `tan/sec`, `sin=0` для `cot/csc`).
3. Система функций:
   - эквивалентные классы по ветвям: `x<=0` и `x>0`;
   - окрестности выколотых точек и асимптот (`x=0`, `x=1`, окрестность `0-`);
   - периодические свойства на тригонометрической ветке;
   - интеграционные этапы со стабами и без.
4. CSV-инфраструктура:
   - выгрузка одного модуля и всех модулей.

Тесты находятся в:

- `src/test/java/lab/pavel/function/trig/`
- `src/test/java/lab/pavel/function/log/`
- `src/test/java/lab/pavel/function/system/`
- `src/test/java/lab/pavel/io/CsvExporterTest.java`
- `src/test/java/lab/pavel/MainTest.java`

## 5. Графики по CSV-выгрузкам

Сгенерированы CSV для всех функций в каталоге:

- `build/reports/csv/`

Сгенерированы графики (SVG) для всех модулей, включая базовые `cos` и `ln`:

- `build/reports/plots/cos.svg`
- `build/reports/plots/sin.svg`
- `build/reports/plots/tan.svg`
- `build/reports/plots/cot.svg`
- `build/reports/plots/sec.svg`
- `build/reports/plots/csc.svg`
- `build/reports/plots/ln.svg`
- `build/reports/plots/log_2.svg`
- `build/reports/plots/log_3.svg`
- `build/reports/plots/log_5.svg`
- `build/reports/plots/log_10.svg`
- `build/reports/plots/system.svg`

Команды воспроизведения:

```bash
./gradlew test
./gradlew generateCsvAll
python scripts/plot_csv.py build/reports/csv build/reports/plots
```

## 6. Выводы

1. Система функций реализована в соответствии с требованиями: базовые функции через ряды, остальные через зависимости.
2. `sin(x)` реализован как зависимый модуль от `cos(x)`, как требовалось по варианту.
3. Проведена поэтапная интеграция по одному модулю с использованием табличных заглушек на Mockito.
4. Тестовое покрытие учитывает эквивалентные классы, экстремальные области, асимптоты, окрестности выколотых точек и периодичность.
5. Для всех модулей получены CSV-выгрузки и графики, пригодные для включения в отчёт.
