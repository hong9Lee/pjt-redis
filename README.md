# 다시 한번 정리하는 Redis  

## 자료구조  
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






















