# 다시 한번 정리하는 Redis  

## 1. 자료구조  
- String
```
최대 512MB 크기의 값을 저장 가능  
- SET key value: 키에 값을 저장  
- GET key: 키의 값을 조회
- INCR key: 값 증가 (정수)
- DECR key: 값 감소 (정수)
- APPEND key value: 문자열 뒤에 값 추가
- SETEX key seconds value: 특정 시간 후 만료되는 값 저장

-> 사용 예시: 캐싱, 카운터, 세션 관리, 분산 락 구현 (SETNX)
```


- List  
양방향 연결 리스트 (Linked List)  
순서가 있는 데이터 저장 가능  
```
- LPUSH, RPUSH → 값 삽입 (앞/뒤)
- LPOP, RPOP → 값 제거 (앞/뒤)
- LRANGE key start stop → 범위 조회
- LREM key count value → 특정 값 삭제

-> 사용 예시: 메시지 큐, 작업 대기열, 채팅 로그 저장
```

- Set  
중복이 없는 데이터 저장  
```
- SADD key value: 값 추가
- SREM key value: 값 삭제
- SMEMBERS key: 모든 값 조회
- SISMEMBER key value: 특정 값 존재 여부 확인
- SUNION key1 key2: 합집합
- SINTER key1 key2: 교집합
- SDIFF key1 key2: 차집합

-> 사용 예시: 유니크한 데이터 저장 (태그, 사용자 ID 저장, 팔로우/팔로워 시스템)
```

- Sorted Set  
Set + Score 기반 정렬 기능  
값에 점수(Score) 를 부여하여 자동 정렬  
```
- ZADD key score value: 값 추가 (점수와 함께)
- ZREM key value: 값 삭제
- ZRANGE key start stop [WITHSCORES]: 범위 조회 (낮은 순)
- ZREVRANGE key start stop [WITHSCORES]: 범위 조회 (높은 순)
- ZRANK key value: 특정 값의 순위 조회 (낮은 순)
- ZREVRANK key value: 특정 값의 순위 조회 (높은 순)

-> 사용 예시: 리더보드, 순위 시스템, 대기열 우선순위 관리
```

- Hash  
Key-Value 형태의 데이터를 저장하는 Map  
작은 객체(예: JSON-like 데이터) 저장에 유리  
```
- HSET key field value: 필드 추가
- HGET key field: 특정 필드 조회
- HGETALL key: 모든 필드와 값 조회
- HDEL key field: 특정 필드 삭제
- HINCRBY key field increment: 값 증가

-> 사용 예시: 사용자 프로필 데이터 저장, 캐싱된 객체 저장
```


- Bitmap  
비트 단위 저장 (0과 1)  
특정 비트 위치를 설정하고 조회 가능  
```
- SETBIT key offset value (0|1): 특정 위치 비트 설정
- GETBIT key offset: 특정 위치 비트 값 가져오기
- BITCOUNT key: 1의 개수 카운트

-> 사용 예시: 출석 체크, 이벤트 참여 여부 관리
```

- HyperLogLog
근사치 중복 제거 (Cardinality Estimation)    
정확한 개수를 저장하지 않고, 약간의 오차를 허용하며 큰 데이터셋의 유니크한 개수를 빠르게 계산  
```
- PFADD key value: 값 추가
- PFCOUNT key: 고유 개수 조회

-> 사용 예시: UV (Unique Visitor) 카운팅, 대규모 데이터 집계
```

- Geospatial  
위도/경도 데이터를 저장하고 거리 계산 가능  
```
- GEOADD key longitude latitude member: 좌표 추가
- GEODIST key member1 member2 [unit]: 두 지점 거리 계산
- GEORADIUS key longitude latitude radius unit: 반경 내 위치 찾기

-> 사용 예시: 가까운 매장 찾기, 배달 서비스
```

- Stream
Kafka와 유사한 메시징 시스템
```
- XADD key * field value: 스트림 메시지 추가
- XREAD COUNT n STREAMS key id: 새로운 메시지 읽기
- XGROUP CREATE key groupname id: 소비자 그룹 생성

-> 사용 예시: 로그 시스템, 실시간 데이터 처리
```


## 2. 레디스의 메모리 관리  

레디스는 In-memory형 데이터 베이스이다.   
이것은 디스크에 정보를 저장하는 RDBMS(MySQL, Oracle)와 다르게 RAM에 데이터를 저장하여 읽기와 쓰기를 할 때 더 빠른 결과를 제공한다.  

- Physical Memory 이상을 사용할 때 Swap 발생
Redis는 In-Memory 데이터베이스이므로 물리 메모리(RAM) 내에서 모든 데이터를 처리해야한다.  
하지만 Redis가 할당된 물리 메모리를 초과하면, 운영체제(OS)는 Swap 공간(디스크의 가상 메모리)을 사용하게 되는데,  
이때 심각한 성능 저하가 발생할 수 있다.  
```
**(1) Redis가 사용할 수 있는 물리 메모리 초과**
- Redis가 사용하는 메모리(used_memory_rss)가 서버의 RAM 용량을 초과하면 Swap이 발생할 수 있음.
- maxmemory 설정이 없거나 너무 크면, Redis가 계속 데이터를 저장하며 메모리가 부족해질 수 있음.

레디스에 Max Memory를 설정해놓으면 랜덤한 Key를 지우거나 Expire 목록에 있는 데이터를 지우고 메모리를 확보해주는 옵션이다.
레디스는 jmalloc을 통해 메모리를 관리를 해주는데 jmalloc은 실제 우리가 1 Byte만 사용한다 하더라도 4096Byte를 할당해주는데 이유는 페이지 단위로 할당을 해주기 때문.
jmalloc에서 메모리를 해제 했다고 하지만 실제로 메모리를 잡고 있는 경우도 있다. 

**(2) OS의 메모리 오버커밋 (Overcommit) 설정 문제**
- 리눅스는 기본적으로 메모리를 과도하게 할당할 수 있도록 설정되어 있음.
- 물리 메모리를 초과하면 Swap이 활성화됨.
해결 방법: overcommit_memory 설정 조정

**(3) Copy-On-Write & 페이징 기법**
Copy-On-Write(COW)는 프로세스가 fork(복제)될 때 메모리 페이지를 공유하다가, 변경이 발생하면 복사하는 기법.
Redis에서 BGSAVE, BGREWRITEAOF 같은 백그라운드 작업을 실행하면 Copy-On-Write(COW) 기법이 적용된다.  

1️⃣ BGSAVE → RDB 스냅샷 저장 시 fork() 실행
BGSAVE는 Redis의 RDB 파일(스냅샷)을 생성하는 과정.
주기적으로 전체 데이터를 디스크에 저장하여, Redis가 재시작될 때 데이터를 복원할 수 있도록 보장함.
실행되면 백그라운드에서 fork()를 실행하여 새로운 프로세스를 생성하고, 기존 데이터를 .rdb 파일로 저장함.

- BGSAVE가 발생하는 이유
1️⃣ save 설정에 따라 자동 실행됨
2️⃣ BGSAVE 명령어를 수동 실행한 경우
3️⃣ Redis가 종료될 때 설정에 따라 자동 실행됨 (stop-writes-on-bgsave-error yes)



2️⃣ BGREWRITEAOF → AOF 파일 리라이트 시 fork() 실행
BGREWRITEAOF는 AOF(Append Only File) 로그를 최적화하는 과정.
Redis는 AOF 방식으로 모든 변경 사항을 파일에 기록하지만, 시간이 지나면서 AOF 파일 크기가 기하급수적으로 커질 수 있음.
이를 방지하기 위해 기존 AOF 파일을 최적화하여 더 작은 크기로 다시 작성하는 과정이 필요함.

- BGREWRITEAOF가 발생하는 이유
1️⃣ AOF 파일이 너무 커지면 자동 실행됨
2️⃣ BGREWRITEAOF 명령을 수동으로 실행한 경우
3️⃣ Redis가 재시작되면서 AOF 파일이 최적화될 필요가 있을 때


🔹 Fork 발생 시 동작 과정  
Redis는 fork()를 호출하여 새로운 프로세스(자식 프로세스) 를 생성함.
이때, 자식 프로세스는 부모 프로세스의 메모리를 그대로 공유하며 데이터를 읽을 수 있음.
하지만, 부모 프로세스에서 새로운 데이터가 추가되거나 변경되면, OS는 변경된 페이지만 복사(Write 시 복사) 하면서 새 메모리를 할당함.
이 과정에서 메모리 사용량이 급격히 증가할 수 있음.

Copy-On-Write로 인해 발생하는 문제  
- 메모리 사용량 급증

Redis가 기존 메모리의 2배까지 필요할 수 있음.
즉, Redis가 4GB 메모리를 사용 중일 때, 추가로 4GB를 더 필요하게 될 수도 있음.
- Swap 발생 가능

Copy-On-Write 과정에서 메모리가 부족하면 OS가 Swap을 사용할 수 있음.
Swap이 발생하면 응답 속도 지연 및 성능 저하로 이어짐.
- 성능 저하

BGSAVE 또는 BGREWRITEAOF 실행 중 대량의 쓰기 작업이 발생하면 Copy-On-Write에 의해
메모리 복사가 빈번해지고, CPU와 메모리 사용량이 급증할 수 있음.


-> 페이징 기법 사용
Redis가 한 번에 많은 데이터를 변경하는 것이 아니라, 작은 단위(페이지)로 나눠서 처리하는 방식.
즉, 데이터를 조금씩 수정하면서 Copy-On-Write가 한 번에 많이 발생하는 것을 방지하는 전략.


(4) 일정 크기의 데이터 사용
메모리 파편화(Fragmentation)를 줄이고, 성능을 최적화하기 위해

- 메모리 파편화 방지
Redis는 데이터를 저장할 때 메모리 블록을 할당받는데, 크기가 크게 변하면 Jemalloc이 재조정(reallocation)하면서 메모리 파편화가 발생할 수 있음.
일정한 크기의 데이터를 유지하면 메모리 블록이 효율적으로 재사용되어 파편화가 줄어듦.

- 데이터 크기가 가변적이면 Copy-On-Write가 심해지고, 불필요한 메모리 복사가 많아져 성능이 저하됨.



```

## 3. 레디스 구조    
Master-Replica는 데이터를 복제(Replication)하여 가용성과 읽기 성능을 높이는 구조.    
✅ 데이터를 여러 개의 Redis 인스턴스에 복제하여, 데이터 손실을 방지하고 읽기 성능을 높이는 구조  
✅ 하나의 마스터(Master)와 하나 이상의 레플리카(Replica)로 구성됨  
✅ 레플리카는 마스터의 데이터를 그대로 복사하며, 쓰기는 마스터에서만 가능  
✅ 트래픽이 많아질 경우 Replica를 추가하여 읽기 부하를 분산할 수 있음
```  
[Master] → [Replica 1]  
 Write     Read  
          [Replica 2]  
            Read  
```

Cluster(샤딩) 은 데이터를 분산(Sharding)하여 하나의 노드에 너무 많은 데이터가 저장되는 걸 방지하는 구조.   
✅ Redis Cluster는 여러 개의 노드가 데이터를 나누어 저장하는 구조 (샤딩)  
✅ 각 노드는 데이터의 일부만 저장하며, 모든 노드가 독립적으로 동작함  
✅ 키(Hash Slot)를 기반으로 데이터가 자동으로 분산 저장됨  
✅ Cluster 모드에서는 노드가 다운되면 자동으로 장애 조치(Failover)됨  
```
[Shard 1]  →  키 범위 0 ~ 5460
[Shard 2]  →  키 범위 5461 ~ 10922
[Shard 3]  →  키 범위 10923 ~ 16383
```















