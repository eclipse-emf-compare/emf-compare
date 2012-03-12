This test is about performance:

The models will be built dynamically in the test execution and the number of the following changes is parameterized:

- N moved classes (from oldPlace to newPlace)
- N new elements (attributes in the moved classes)
- N deleted operations (with two parameters each)
- N changed attributes (abstract false -> true of the moved classes)
- N changed references (references of moved classes change type from OldReferenceTarget to NewReferenceTarget)
- N added references (all moved classes inherit now from SuperType)

A run might for example compute differences with N = 10..100
Hence, the total number of changes is: 6*N

(un)changed-2.ecore is an instance for manual testing purposes.

------------------------------------------------------------------------------
Optimizations so far:

Initial version:
 Classes | Built | Emfdiff |    MPatch | Groups | Deps | Resolve | Copy |  Apply | Changes | Check | Total
---------+-------+---------+-----------+--------+------+---------+------+--------+---------+-------+-------
       5 |     0 |     141 |      1128 |     16 |    0 |     438 |    0 |    329 |      16 |    16 |  2131
      10 |     0 |      31 |       141 |      0 |    0 |     485 |    0 |   1974 |      31 |    16 |  2678
      15 |    16 |      62 |       110 |     16 |    0 |     705 |    0 |   5936 |      47 |    47 |  6939
      20 |     0 |      63 |       141 |      0 |   16 |     955 |    0 |  14207 |      79 |    62 | 15523
      25 |     0 |      94 |       141 |     16 |    0 |    1331 |    0 |  28759 |      94 |    79 | 30514


Caching queries built from OCL String expressions:
 Classes | Built | Emfdiff |    MPatch | Groups | Deps | Resolve | Copy |  Apply | Changes | Check | Total
---------+-------+---------+-----------+--------+------+---------+------+--------+---------+-------+-------
      10 |     0 |     219 |      1207 |     15 |    0 |     314 |    0 |     93 |      47 |    32 |  1974
      20 |     0 |      78 |       188 |      0 |   16 |     297 |    0 |    423 |      63 |    63 |  1128
      30 |    15 |     110 |       188 |      0 |   16 |     595 |   15 |   1316 |     126 |    94 |  2475
      40 |    15 |     188 |       204 |      0 |   15 |     940 |    0 |   3055 |     188 |   141 |  4746
      50 |    15 |     298 |       204 |     15 |   16 |    1441 |    0 |   5733 |     266 |   220 |  8208
      60 |     0 |     376 |       235 |     15 |   32 |    2052 |    0 |   9899 |     376 |   298 | 13283
      70 |     0 |     485 |       251 |     31 |   32 |    2725 |    0 |  15507 |     470 |   392 | 19893
      80 |    16 |     595 |       282 |     31 |   63 |    3524 |    0 |  23042 |     611 |   501 | 28665
      90 |     0 |     736 |       314 |     47 |   62 |    4418 |    0 |  32722 |     720 |   627 | 39646
     100 |     0 |     877 |       360 |     63 |   94 |    5435 |    0 |  45348 |     830 |   752 | 53759

      
Caching the entire OCL condition (which contains the expression as a query):
 Classes | Built | Emfdiff |    MPatch | Groups | Deps | Resolve | Copy |  Apply | Changes | Check |  Total
---------+-------+---------+-----------+--------+------+---------+------+--------+---------+-------+--------
      10 |    15 |     194 |      1104 |     14 |    0 |     284 |    0 |     30 |      30 |    29 |   1745
      20 |     0 |      90 |       164 |      0 |   15 |     253 |    0 |     60 |      60 |    59 |    701
      30 |     0 |     135 |       179 |      0 |   15 |     522 |    0 |    149 |     119 |   105 |   1224
      40 |     0 |     194 |       179 |     14 |   15 |     880 |    0 |    343 |     179 |   165 |   1969
      50 |     0 |     283 |       194 |     15 |   30 |    1312 |    0 |    612 |     268 |   209 |   2923
      60 |    15 |     358 |       224 |     15 |   30 |    1834 |    0 |   1074 |     358 |   299 |   4207
      70 |     0 |     477 |       239 |     29 |   30 |    2491 |    0 |   1686 |     462 |   403 |   5817
      80 |     0 |     597 |       283 |     30 |   45 |    3281 |    0 |   2506 |     567 |   507 |   7816
      90 |     0 |     731 |       298 |     30 |   60 |    4087 |    0 |   3505 |     686 |   627 |  10024
     100 |     0 |     835 |       343 |     60 |   60 |    5011 |    0 |   4878 |     820 |   716 |  12723
     110 |     0 |     985 |       373 |     89 |   90 |    6056 |    0 |   6488 |     940 |   850 |  15871
     120 |     0 |    1179 |       388 |    104 |  104 |    7175 |    0 |   8398 |    1104 |   999 |  19451
     130 |     0 |    1328 |       432 |    135 |  149 |    8457 |    0 |  10665 |    1119 |  1208 |  23493
     140 |    15 |    1507 |       462 |    164 |  164 |    9756 |    0 |  13380 |    1297 |  1328 |  28073
     150 |     0 |    1685 |       508 |    223 |  254 |   11127 |    0 |  16573 |    1523 |  1785 |  33678


Caching the resolution of equally resolving references:
 Diff's | Built | Emfdiff |    MPatch | Groups | Deps | Refs | Resolve | Copy |  Apply | Changes | Check |  Total | Transformation
--------+-------+---------+-----------+--------+------+------+---------+------+--------+---------+-------+--------+----------------
     60 |     0 |     156 |      1016 |      0 |   16 |    0 |      16 |   15 |     63 |      31 |    47 |   1360 | descriptor: Default, symref: ID-based
     60 |     0 |      32 |       250 |      0 |    0 |   15 |     250 |    0 |     78 |      32 |    47 |    689 | descriptor: Default, symref: Condition-based
    120 |    15 |      94 |       187 |      0 |    0 |   16 |       0 |    0 |     47 |     109 |    78 |    530 | descriptor: Default, symref: ID-based
    120 |     0 |     109 |       157 |      0 |   15 |    0 |     141 |    0 |     62 |      94 |    63 |    641 | descriptor: Default, symref: Condition-based
    180 |     0 |     172 |       203 |      0 |   16 |    0 |      47 |    0 |     62 |     157 |   125 |    782 | descriptor: Default, symref: ID-based
    180 |     0 |     172 |       203 |      0 |   16 |    0 |     187 |    0 |    438 |     125 |   109 |   1250 | descriptor: Default, symref: Condition-based
    240 |     0 |     219 |       125 |     16 |    0 |    0 |      31 |    0 |    109 |     203 |   157 |    860 | descriptor: Default, symref: ID-based
    240 |    15 |     203 |       157 |     15 |    0 |   16 |     250 |    0 |    187 |     203 |   157 |   1187 | descriptor: Default, symref: Condition-based
    300 |    15 |     282 |       156 |     15 |   16 |    0 |      47 |    0 |    141 |     296 |   235 |   1203 | descriptor: Default, symref: ID-based
    300 |    15 |     297 |       203 |     16 |   16 |   15 |     360 |   15 |    282 |     281 |   250 |   1735 | descriptor: Default, symref: Condition-based
    360 |    15 |     407 |       187 |     16 |   15 |   16 |      47 |   16 |    203 |     390 |   328 |   1624 | descriptor: Default, symref: ID-based
    360 |     0 |     407 |       218 |     16 |   31 |    0 |     516 |   16 |    390 |     406 |   329 |   2329 | descriptor: Default, symref: Condition-based
    420 |     0 |     500 |       203 |     32 |   31 |    0 |      94 |    0 |    250 |     546 |   438 |   2094 | descriptor: Default, symref: ID-based
    420 |     0 |     531 |       250 |     15 |   47 |    0 |     688 |   15 |    516 |     531 |   453 |   3046 | descriptor: Default, symref: Condition-based
    480 |    16 |     672 |       219 |     31 |   47 |    0 |     109 |    0 |    360 |     671 |   625 |   2750 | descriptor: Default, symref: ID-based
    480 |     0 |     688 |       281 |     31 |   63 |   15 |     891 |    0 |    625 |     640 |   547 |   3766 | descriptor: Default, symref: Condition-based
    540 |     0 |     782 |       250 |     46 |   63 |    0 |     109 |   16 |    406 |     750 |   672 |   3094 | descriptor: Default, symref: ID-based
    540 |    16 |     765 |       282 |     46 |   63 |   16 |    1046 |    0 |    813 |     750 |   687 |   4468 | descriptor: Default, symref: Condition-based
    600 |    16 |     922 |       250 |     78 |   63 |   15 |     156 |    0 |    500 |     875 |   782 |   3642 | descriptor: Default, symref: ID-based
    600 |    16 |     906 |       313 |     62 |   78 |   16 |    1266 |    0 |   1000 |     890 |   828 |   5359 | descriptor: Default, symref: Condition-based
    660 |    16 |    1078 |       281 |     94 |  109 |   16 |     172 |    0 |    625 |    1016 |   953 |   4344 | descriptor: Default, symref: ID-based
    660 |     0 |    1078 |       329 |     93 |   78 |   32 |    1531 |    0 |   1187 |    1032 |   922 |   6250 | descriptor: Default, symref: Condition-based
    720 |     0 |    1250 |       313 |    125 |  125 |   15 |     203 |    0 |    719 |    1141 |  1125 |   5001 | descriptor: Default, symref: ID-based
    720 |     0 |    1250 |       375 |    110 |  125 |   15 |    1782 |    0 |   1406 |    1172 |  1094 |   7314 | descriptor: Default, symref: Condition-based
    780 |     0 |    1438 |       343 |    110 |  109 |   16 |     250 |    0 |    828 |    1281 |  1281 |   5640 | descriptor: Default, symref: ID-based
    780 |     0 |    1438 |       406 |    140 |  157 |   15 |    2110 |    0 |   1625 |    1281 |  1281 |   8438 | descriptor: Default, symref: Condition-based
    840 |     0 |    1656 |       391 |    172 |  187 |   16 |     265 |   16 |    953 |    1328 |  1485 |   6453 | descriptor: Default, symref: ID-based
    840 |     0 |    1610 |       453 |    172 |  187 |   16 |    2406 |    0 |   1906 |    1375 |  1500 |   9609 | descriptor: Default, symref: Condition-based
    
    