## Setting up kafka cluster
```bash
# resources 디렉터리로 이동
cd messaging-system\src\main\resources
# docker-compose 실행
docker-compose up -d 
```

## Memo
- Topic 과 Consumer Group
```
* Topic: 여러개의 파티션으로 구성 된다.
* Consumer Group: 여러개의 컨슈머로 구성되며 동일한 그룹 아이디를 갖는다.

컨슈머 그룹은 여러 컨슈머로 구성된 그룹으로, 동일한 그룹 ID를 공유하며 동일한 토픽을 구독한다.
하나의 파티션은 한 번에 하나의 컨슈머에게만 할당되며, 한 컨슈머는 여러 파티션을 처리할 수 있지만,
하나의 파티션이 동시에 여러 컨슈머에게 할당되지 않는다.
```

## Reference
 - https://victorydntmd.tistory.com/343 // 메시징 시스템의 이해
