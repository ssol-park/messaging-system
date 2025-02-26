### 01. Project clone
`git clone https://github.com/ssol-park/messaging-system.git`

### 02.Run
```./rabbit_start.sh``` : rabbitmq container 실행

```./app_start.sh``` : 애플리케이션 실행

### Settings
 #### RabbitMQ 클러스터 구성

1.ErlangCookie 동기화
   
``` 위치 /var/lib/rabbitmq/.erlang.cookie (컨테이너 내부) ```
    

2.클러스터 구성 (모든 작업은 해당 노드의 컨테이너 내부에서 진행)
   ``` 
    1. master node name 조회 (현재 rabbitmq1)
     - rabbitmqctl cluster_status
     ** 옵션 `| grep cluster_name`
   
    2. slave node 설정 
     - rabbitmqctl stop_app // 클러스터 구성을 위해 컨테이너 stop 후 설정 진행
     - rabbitmqctl join_cluster rabbit@<마스터노드>
     - rabbitmqctl start_app
       
    3. 클러스터 연결 확인
     - rabbitmqctl cluster_status 혹은 GUI 확인
   ```

 #### Ref
 - https://www.rabbitmq.com/docs
 - https://www.rabbitmq.com/docs/3.13/ha
 - https://www.rabbitmq.com/docs/parameters#policies
 