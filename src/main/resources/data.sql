INSERT INTO group_info(id, ordinal,region,ban) values(1,11,'구미',4);

INSERT INTO question(question) values ("당신이 어릴적 살던 동네는?"),
                                      ("당신의 초등학교 담임 선생님 이름은?"),
                                      ("1학기 반장 이름은?") ,
                                      ("당신의 1학기 관통 프로젝트 페어는?") ,
                                      ("당신의 1학기 강사님 이름은?"),
                                      ("당신의 2학기 공통 프로젝트 주제는?"),
                                      ("싸피를 하면서 가장 인상깊던 일은?");

INSERT INTO member(id,sub,provider,groupInfoId,name,serialNumber,questionId,answer) values(10001, '102828872935030969646' , 'google', 1, '김민종','KIMMINJONG',1,'구미');
INSERT INTO member(id,sub,provider,groupInfoId,name,serialNumber,questionId,answer) values(10002, '113049760373373067515' , 'google', 1, '이지언','LEEJIEON',1,'구미');
INSERT INTO member(id,sub,provider,groupInfoId,name,serialNumber,questionId,answer) values(10003, '113319945388395204387' , 'google', 1, '이예찬','LEEYECHAN',1,'구미');