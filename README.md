# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 우아한테크코스 코드리뷰
* [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)


## DB

- [도커 다운로드](https://www.docker.com/products/docker-desktop)

### 실행 방법

1. IntelliJ IDEA에서 `docker-compose.yml`로 이동 후, Run 버튼 클릭
2. 혹은 프로젝트 디렉터리에서 아래의 명령어를 터미널에 입력

```bash
cd docker
docker-compose up -d
```

- 대용량 데이터 처리를 위한 Dataset Download

> [stack-overflow-2018-developer-survey](https://www.kaggle.com/stackoverflow/stack-overflow-2018-developer-survey)

- 아래의 메뉴얼을 확인하여 다운받은 CSV파일을 import한다.

> https://dev.mysql.com/doc/workbench/en/wb-admin-export-import-table.html

## 규칙
### @configuration
- class에만 쓸 수 있다.
- configuration annotation에 bean annotation을 포함한다.

### @bean
- method, @interface, class에 붙일 수 있다.
    - method에 붙이는 경우: method 리턴 값을 빈으로 등록한다.
    - @interface에 붙이는 경우: @interface가 붙여진 모든 class를 빈으로 등록한다.
    - class에 붙이는 경우, class를 빈으로 등록한다.
    
### @ComponentScan
- @configuration이 있는 class에만 쓸 수 있다.
- @configuration에 componentScan이 없는 경우: 에러를 터뜨린다.
- 

