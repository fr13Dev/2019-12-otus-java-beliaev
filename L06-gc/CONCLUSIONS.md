# Результаты
| GC Name               |                   |                |               |                   |                |               |
|-----------------------|-------------------|----------------|---------------|-------------------|----------------|---------------|
| Minutes after run app |                   | 256 Mb         |               |                   | 512 Mb         |               |
|                       | Invokes           | Spent time, ms | Efficiency, % | Invokes           | Spent time, ms | Efficiency, % |
| **Serial**            |                   |                |               |                   |                |               |
| First minute          | 3 minor           | 284            | 99\.526667    | 1 minor           | 130            | 99\.783333    |
| Second minute         | 3 minor, 4 major  | 1813           | 96\.781667    | 2 minor           | 448            | 99\.253333    |
| Third minute          |                   | oom            | oom           | 3 minor, 2 major  | 1762           | 97\.063333    |
| **Parallel**          |                   |                |               |                   |                |               |
| First minute          | 1 minor           | 652            | 98\.913333    | 1 minor           | 178            | 99\.703333    |
| Second minute         |                   | oom            | oom           | 2 minor           | 441            | 99\.265000    |
| Third minute          |                   |                |               | 3 minor, 1 major  | 2534           | 95\.776667    |
| **G1**                |                   |                |               |                   |                |               |
| First minute          | 10 major          | 476            | 99\.206667    | 7 minor           | 445            | 99\.258333    |
| Second minute         |                   | oom            | oom           | 6 minor           | 402            | 99\.330000    |
| Third minute          |                   |                |               | 4 minor           | 413            | 99\.311667    |
# Выводы
* Чем меньше программа потребляет памяти, тем лучше использовать **Serial GC/Parallel**
* Чем больше программа потребляет памяти, тем о лучше использовать **G1**
* Если требуется low latency, то **G1** лучшеK