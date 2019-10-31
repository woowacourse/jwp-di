# todo

bean factory가 하는 일
- 초기 상태: annotation이 써져 있는 class들을 매개변수로 받는다.

1. class 하나마다 순차적으로 constructor를 가져온다.
2. 첫 번째 constructor의 매개변수에 참조변수가 있는지 확인한다.