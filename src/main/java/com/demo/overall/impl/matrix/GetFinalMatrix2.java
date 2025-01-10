package com.demo.overall.impl.matrix;

import com.Bean.PathResult;
import com.Bean.RealNodeInfo;
import com.demo.overall.NewDemoRun.meetRelation.CalculateGroupSim;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 根据权值排序的结果:修改后
 0:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [0, 5, 6, 2, 0] | Weight: 1.0
 1:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [3, 4, 0, 1, 3] | Weight: 1.0
 2:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [3, 4, 1, 0, 3] | Weight: 1.0
 3:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [3, 0, 4, 1, 3] | Weight: 1.0
 4:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [3, 0, 1, 4, 3] | Weight: 1.0
 5:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [0, 2, 6, 5, 0] | Weight: 1.0
 6:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [3, 1, 4, 0, 3] | Weight: 1.0
 7:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [3, 1, 0, 4, 3] | Weight: 1.0
 8:Path: 111 -> 110 -> 112 -> 113 -> 111 | Indices: [4, 3, 0, 1, 4] | Weight: 1.0
 9:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [4, 3, 1, 0, 4] | Weight: 1.0
 10:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [5, 0, 2, 6, 5] | Weight: 1.0
 11:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 0, 3, 1, 4] | Weight: 1.0
 12:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [4, 0, 1, 3, 4] | Weight: 1.0
 13:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [5, 6, 2, 0, 5] | Weight: 1.0
 14:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 3, 0, 4] | Weight: 1.0
 15:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [4, 1, 0, 3, 4] | Weight: 1.0
 16:Path: 112 -> 110 -> 111 -> 113 -> 112 | Indices: [0, 3, 4, 1, 0] | Weight: 1.0
 17:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [0, 3, 1, 4, 0] | Weight: 1.0
 18:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [0, 4, 3, 1, 0] | Weight: 1.0
 19:Path: 112 -> 111 -> 113 -> 110 -> 112 | Indices: [0, 4, 1, 3, 0] | Weight: 1.0
 20:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [6, 5, 0, 2, 6] | Weight: 1.0
 21:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 3, 4, 0] | Weight: 1.0
 22:Path: 112 -> 113 -> 111 -> 110 -> 112 | Indices: [0, 1, 4, 3, 0] | Weight: 1.0
 23:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [6, 2, 0, 5, 6] | Weight: 1.0
 24:Path: 113 -> 110 -> 111 -> 112 -> 113 | Indices: [1, 3, 4, 0, 1] | Weight: 1.0
 25:Path: 113 -> 110 -> 112 -> 111 -> 113 | Indices: [1, 3, 0, 4, 1] | Weight: 1.0
 26:Path: 113 -> 110 -> 111 -> 112 -> 113 | Indices: [2, 0, 5, 6, 2] | Weight: 1.0
 27:Path: 113 -> 111 -> 110 -> 112 -> 113 | Indices: [1, 4, 3, 0, 1] | Weight: 1.0
 28:Path: 113 -> 111 -> 112 -> 110 -> 113 | Indices: [1, 4, 0, 3, 1] | Weight: 1.0
 29:Path: 113 -> 112 -> 110 -> 111 -> 113 | Indices: [1, 0, 3, 4, 1] | Weight: 1.0
 30:Path: 113 -> 112 -> 111 -> 110 -> 113 | Indices: [1, 0, 4, 3, 1] | Weight: 1.0
 31:Path: 113 -> 112 -> 111 -> 110 -> 113 | Indices: [2, 6, 5, 0, 2] | Weight: 1.0
 32:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 0, 1, 4] | Weight: 0.875
 33:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 10, 21, 4] | Weight: 0.875
 34:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 1, 0, 16, 4] | Weight: 0.875
 35:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 21, 10, 16, 4] | Weight: 0.875
 36:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [16, 4, 1, 0, 16] | Weight: 0.875
 37:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [16, 4, 21, 10, 16] | Weight: 0.875
 38:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [16, 0, 1, 4, 16] | Weight: 0.875
 39:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [16, 10, 21, 4, 16] | Weight: 0.875
 40:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [0, 16, 4, 1, 0] | Weight: 0.875
 41:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [10, 16, 4, 21, 10] | Weight: 0.875
 42:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 4, 16, 0] | Weight: 0.875
 43:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [10, 21, 4, 16, 10] | Weight: 0.875
 44:Path: 113 -> 110 -> 111 -> 112 -> 113 | Indices: [1, 4, 16, 0, 1] | Weight: 0.875
 45:Path: 113 -> 110 -> 111 -> 112 -> 113 | Indices: [21, 4, 16, 10, 21] | Weight: 0.875
 46:Path: 113 -> 112 -> 111 -> 110 -> 113 | Indices: [1, 0, 16, 4, 1] | Weight: 0.875
 47:Path: 113 -> 112 -> 111 -> 110 -> 113 | Indices: [21, 10, 16, 4, 21] | Weight: 0.875
 48:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [0, 5, 2, 6, 0] | Weight: 0.765625
 49:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [3, 18, 0, 1, 3] | Weight: 0.765625
 50:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 4, 0, 1, 4] | Weight: 0.765625
 51:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [4, 16, 21, 10, 4] | Weight: 0.765625
 52:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 4, 1, 0] | Weight: 0.765625
 53:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 5, 2, 0] | Weight: 0.765625
 54:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 15, 16, 0] | Weight: 0.765625
 55:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [0, 6, 2, 5, 0] | Weight: 0.765625
 56:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [3, 0, 16, 21, 3] | Weight: 0.765625
 57:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [4, 10, 21, 16, 4] | Weight: 0.765625
 58:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 0, 4, 1, 19] | Weight: 0.765625
 59:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 12, 4, 1, 19] | Weight: 0.765625
 60:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 1, 4, 6, 0] | Weight: 0.765625
 61:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 2, 5, 6, 0] | Weight: 0.765625
 62:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 16, 15, 6, 0] | Weight: 0.765625
 63:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [3, 1, 0, 18, 3] | Weight: 0.765625
 64:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [3, 21, 16, 0, 3] | Weight: 0.765625
 65:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 1, 0, 4, 4] | Weight: 0.765625
 66:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 1, 4, 0, 19] | Weight: 0.765625
 67:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 1, 4, 12, 19] | Weight: 0.765625
 68:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [4, 4, 1, 0, 4] | Weight: 0.765625
 69:Path: 111 -> 110 -> 112 -> 113 -> 111 | Indices: [5, 0, 6, 2, 5] | Weight: 0.765625
 70:Path: 111 -> 110 -> 112 -> 113 -> 111 | Indices: [16, 4, 10, 21, 16] | Weight: 0.765625
 71:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [18, 3, 1, 0, 18] | Weight: 0.765625
 72:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 0, 19, 1, 4] | Weight: 0.765625
 73:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [4, 0, 1, 4, 4] | Weight: 0.765625
 74:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 6, 0, 1, 4] | Weight: 0.765625
 75:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 12, 19, 1, 4] | Weight: 0.765625
 76:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [5, 6, 0, 2, 5] | Weight: 0.765625
 77:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [15, 6, 0, 16, 15] | Weight: 0.765625
 78:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [16, 0, 3, 21, 16] | Weight: 0.765625
 79:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [18, 0, 1, 3, 18] | Weight: 0.765625
 80:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 0, 6, 4] | Weight: 0.765625
 81:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 19, 0, 4] | Weight: 0.765625
 82:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 19, 12, 4] | Weight: 0.765625
 83:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [5, 2, 0, 6, 5] | Weight: 0.765625
 84:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [5, 2, 6, 0, 5] | Weight: 0.765625
 85:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [15, 16, 0, 6, 15] | Weight: 0.765625
 86:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [16, 21, 3, 0, 16] | Weight: 0.765625
 87:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [16, 21, 10, 4, 16] | Weight: 0.765625
 88:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [0, 3, 21, 16, 0] | Weight: 0.765625
 89:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [0, 19, 1, 4, 0] | Weight: 0.765625
 90:Path: 112 -> 110 -> 111 -> 113 -> 112 | Indices: [6, 0, 5, 2, 6] | Weight: 0.765625
 91:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [6, 0, 1, 4, 6] | Weight: 0.765625
 92:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [6, 0, 2, 5, 6] | Weight: 0.765625
 93:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [6, 0, 16, 15, 6] | Weight: 0.765625
 94:Path: 112 -> 110 -> 111 -> 113 -> 112 | Indices: [10, 4, 16, 21, 10] | Weight: 0.765625
 95:Path: 112 -> 110 -> 113 -> 111 -> 112 | Indices: [12, 19, 1, 4, 12] | Weight: 0.765625
 96:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [0, 4, 4, 1, 0] | Weight: 0.765625
 97:Path: 112 -> 111 -> 113 -> 110 -> 112 | Indices: [0, 4, 1, 19, 0] | Weight: 0.765625
 98:Path: 112 -> 111 -> 113 -> 110 -> 112 | Indices: [0, 16, 21, 3, 0] | Weight: 0.765625
 99:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [0, 18, 3, 1, 0] | Weight: 0.765625
 根据权值排序的结果:修改前
 根据权值排序的结果:
 0:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [0, 5, 6, 2, 0] | Weight: 1.0
 1:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [3, 4, 0, 1, 3] | Weight: 1.0
 2:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [3, 4, 1, 0, 3] | Weight: 1.0
 3:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [3, 0, 4, 1, 3] | Weight: 1.0
 4:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [3, 0, 1, 4, 3] | Weight: 1.0
 5:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [0, 2, 6, 5, 0] | Weight: 1.0
 6:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [3, 1, 4, 0, 3] | Weight: 1.0
 7:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [3, 1, 0, 4, 3] | Weight: 1.0
 8:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 0, 3, 1, 4] | Weight: 1.0
 9:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [4, 0, 1, 3, 4] | Weight: 1.0
 10:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [5, 6, 2, 0, 5] | Weight: 1.0
 11:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 3, 0, 4] | Weight: 1.0
 12:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [4, 1, 0, 3, 4] | Weight: 1.0
 13:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 3, 4, 0] | Weight: 1.0
 14:Path: 112 -> 113 -> 111 -> 110 -> 112 | Indices: [0, 1, 4, 3, 0] | Weight: 1.0
 15:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [6, 2, 0, 5, 6] | Weight: 1.0
 16:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 0, 1, 4] | Weight: 0.875
 17:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 10, 21, 4] | Weight: 0.875
 18:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 1, 0, 16, 4] | Weight: 0.875
 19:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 21, 10, 16, 4] | Weight: 0.875
 20:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [16, 0, 1, 4, 16] | Weight: 0.875
 21:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [16, 10, 21, 4, 16] | Weight: 0.875
 22:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 4, 16, 0] | Weight: 0.875
 23:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [10, 21, 4, 16, 10] | Weight: 0.875
 24:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [0, 5, 2, 6, 0] | Weight: 0.765625
 25:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [3, 18, 0, 1, 3] | Weight: 0.765625
 26:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 4, 0, 1, 4] | Weight: 0.765625
 27:Path: 110 -> 111 -> 113 -> 112 -> 110 | Indices: [4, 16, 21, 10, 4] | Weight: 0.765625
 28:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 4, 1, 0] | Weight: 0.765625
 29:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 5, 2, 0] | Weight: 0.765625
 30:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 15, 16, 0] | Weight: 0.765625
 31:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [0, 6, 2, 5, 0] | Weight: 0.765625
 32:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [3, 0, 16, 21, 3] | Weight: 0.765625
 33:Path: 110 -> 112 -> 113 -> 111 -> 110 | Indices: [4, 10, 21, 16, 4] | Weight: 0.765625
 34:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 0, 4, 1, 19] | Weight: 0.765625
 35:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 12, 4, 1, 19] | Weight: 0.765625
 36:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 1, 4, 6, 0] | Weight: 0.765625
 37:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 2, 5, 6, 0] | Weight: 0.765625
 38:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 16, 15, 6, 0] | Weight: 0.765625
 39:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [3, 1, 0, 18, 3] | Weight: 0.765625
 40:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [3, 21, 16, 0, 3] | Weight: 0.765625
 41:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 1, 0, 4, 4] | Weight: 0.765625
 42:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 1, 4, 0, 19] | Weight: 0.765625
 43:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 1, 4, 12, 19] | Weight: 0.765625
 44:Path: 111 -> 110 -> 113 -> 112 -> 111 | Indices: [4, 4, 1, 0, 4] | Weight: 0.765625
 45:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 0, 19, 1, 4] | Weight: 0.765625
 46:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [4, 0, 1, 4, 4] | Weight: 0.765625
 47:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 6, 0, 1, 4] | Weight: 0.765625
 48:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [4, 12, 19, 1, 4] | Weight: 0.765625
 49:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [5, 6, 0, 2, 5] | Weight: 0.765625
 50:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [15, 6, 0, 16, 15] | Weight: 0.765625
 51:Path: 111 -> 112 -> 110 -> 113 -> 111 | Indices: [16, 0, 3, 21, 16] | Weight: 0.765625
 52:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [18, 0, 1, 3, 18] | Weight: 0.765625
 53:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 0, 6, 4] | Weight: 0.765625
 54:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 19, 0, 4] | Weight: 0.765625
 55:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [4, 1, 19, 12, 4] | Weight: 0.765625
 56:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [5, 2, 0, 6, 5] | Weight: 0.765625
 57:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [5, 2, 6, 0, 5] | Weight: 0.765625
 58:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [15, 16, 0, 6, 15] | Weight: 0.765625
 59:Path: 111 -> 113 -> 110 -> 112 -> 111 | Indices: [16, 21, 3, 0, 16] | Weight: 0.765625
 60:Path: 111 -> 113 -> 112 -> 110 -> 111 | Indices: [16, 21, 10, 4, 16] | Weight: 0.765625
 61:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [5, 6, 19, 8, 5] | Weight: 0.765625
 62:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [10, 6, 14, 21, 10] | Weight: 0.765625
 63:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [15, 6, 14, 21, 15] | Weight: 0.765625
 64:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 3, 18, 0] | Weight: 0.765625
 65:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [0, 1, 4, 4, 0] | Weight: 0.765625
 66:Path: 112 -> 113 -> 111 -> 110 -> 112 | Indices: [6, 2, 5, 0, 6] | Weight: 0.765625
 67:Path: 112 -> 113 -> 111 -> 110 -> 112 | Indices: [10, 21, 16, 4, 10] | Weight: 0.765625
 68:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [3, 4, 6, 2, 3] | Weight: 0.75
 69:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 6, 2, 4] | Weight: 0.75
 70:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [3, 2, 6, 4, 3] | Weight: 0.75
 71:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 2, 6, 16, 4] | Weight: 0.75
 72:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [4, 6, 2, 3, 4] | Weight: 0.75
 73:Path: 111 -> 112 -> 113 -> 110 -> 111 | Indices: [16, 6, 2, 4, 16] | Weight: 0.75
 74:Path: 112 -> 111 -> 110 -> 113 -> 112 | Indices: [5, 6, 14, 8, 5] | Weight: 0.75
 75:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [6, 2, 3, 4, 6] | Weight: 0.75
 76:Path: 112 -> 113 -> 110 -> 111 -> 112 | Indices: [6, 2, 4, 16, 6] | Weight: 0.75
 77:Path: 113 -> 110 -> 111 -> 112 -> 113 | Indices: [8, 4, 16, 5, 8] | Weight: 0.75
 78:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [4, 16, 11, 16, 4] | Weight: 0.669921875
 79:Path: 110 -> 111 -> 112 -> 113 -> 110 | Indices: [8, 21, 0, 1, 8] | Weight: 0.669921875
 80:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 5, 16, 0] | Weight: 0.669921875
 81:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 16, 21, 0] | Weight: 0.669921875
 82:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 18, 8, 0] | Weight: 0.669921875
 83:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [0, 6, 18, 13, 0] | Weight: 0.669921875
 84:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [4, 5, 16, 21, 4] | Weight: 0.669921875
 85:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [4, 10, 16, 21, 4] | Weight: 0.669921875
 86:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 0, 16, 21, 19] | Weight: 0.669921875
 87:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 12, 16, 21, 19] | Weight: 0.669921875
 88:Path: 110 -> 112 -> 111 -> 113 -> 110 | Indices: [19, 12, 18, 8, 19] | Weight: 0.669921875
 89:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 8, 18, 6, 0] | Weight: 0.669921875
 90:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 13, 18, 6, 0] | Weight: 0.669921875
 91:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 16, 5, 6, 0] | Weight: 0.669921875
 92:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [0, 21, 16, 6, 0] | Weight: 0.669921875
 93:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [4, 16, 11, 16, 4] | Weight: 0.669921875
 94:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [4, 21, 16, 5, 4] | Weight: 0.669921875
 95:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [4, 21, 16, 10, 4] | Weight: 0.669921875
 96:Path: 110 -> 113 -> 112 -> 111 -> 110 | Indices: [8, 1, 0, 21, 8] | Weight: 0.669921875
 97:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 8, 18, 12, 19] | Weight: 0.669921875
 98:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 21, 16, 0, 19] | Weight: 0.669921875
 99:Path: 110 -> 113 -> 111 -> 112 -> 110 | Indices: [19, 21, 16, 12, 19] | Weight: 0.669921875


 */
@Component
public class GetFinalMatrix2 {
    public  static int TIMES = 4;

    public static void main(String[] args) {
        GetFinalMatrix2 getFinalMatrix = new GetFinalMatrix2();

        List<PathResult> resulyList = getFinalMatrix.getResulyList();
        int i = 0;
        // 输出前N个结果
        System.out.println("根据权值排序的结果:");
        if (resulyList.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (PathResult result : resulyList) {
                System.out.println(i++ +":"+result);
                if (i==100){
                    break;
                }
            }
        }
    }
  public static  Map<Integer, List<RealNodeInfo>> sketchToRealMap = new CalculateGroupSim().firstFilter(); // 对应的结果下标

    public   List<PathResult> getResulyList() {
        MatrixMerger m = new MatrixMerger();
        Map<String, double[][]> matrixMerger = m.getMatrixMerger();

        List<PathResult> results = new ArrayList<PathResult>();

        // 使用 Set 获取不重复的键
        Set<String> uniqueKeys = new HashSet<>();

        // 遍历每个比较矩阵作为起始点
        for (String key : matrixMerger.keySet()) {
            String start = key.split("-")[0]; // 获取起始矩阵的行
            String end = key.split("-")[1]; // 获取起始矩阵的行
          uniqueKeys.add(start);
          uniqueKeys.add(end);
        }

        Map<String, Boolean> visited = new HashMap<>(); //还是会导致 Path: 110 -> 111 -> 110 -> 113 -> 110 | Indices: [0, 2, 2, 1, 0] | Weight: 0.875
        // 以每个不同的为起点
        for (String start : uniqueKeys) {
            visited.clear();
            visited.put("110",false);
            visited.put("111",false);
            visited.put("112",false);
            visited.put("113",false);
           // System.out.println("Starting from: " + start);
            dfs(uniqueKeys,matrixMerger, start, 4, visited,new ArrayList<>(), new ArrayList<>(), results,1);
        }

        // 根据权值对路径结果进行排序
        results.sort((a, b) -> Double.compare(b.getWeight(), a.getWeight()));

        return results;
    }
    private static void dfs(Set<String> uniqueKeys, Map<String, double[][]> matrixMerger, String current, int remaining, Map<String, Boolean> visited,
                            List<String> path, List<Integer> indexList, List<PathResult> results, double currentWeight) {
        path.add(current); // 添加当前节点到路径中

        if (remaining == 0) { // 如果没有剩余的节点，保存路径结果
            results.add(new PathResult(new ArrayList<>(path), new ArrayList<>(indexList), currentWeight));
        } else if (remaining == TIMES) {
            // 第一次
            for (String nextKey : uniqueKeys) {
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                    if (matrixMerger.containsKey(forwardKey)) {
                        double[][] matrix = matrixMerger.get(forwardKey);
                        if (matrix != null) {
                            for (int i = 0; i < matrix.length; i++) {
                                for (int j = 0; j < matrix[0].length; j++) {
                                    double weightToAdd = matrix[i][j];
                                    if (weightToAdd != 0) {
                                        currentWeight *= weightToAdd;
                                        //currentWeight += weightToAdd;
                                        indexList.add(i);
                                        indexList.add(j);
                                        visited.put(current, true); // 在递归之前标记为已访问
                                        visited.put(nextKey, true); // 在递归之前标记为已访问
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        visited.put(nextKey, false); // 回溯后标记为未访问
                                        visited.put(current, false); // 在递归之前标记为已访问
                                        currentWeight /= weightToAdd;
                                        //currentWeight -= weightToAdd;
                                        indexList.remove(indexList.size() - 1);
                                        indexList.remove(indexList.size() - 1);
                                    }
                                }
                            }
                        }
                    }

                    if (matrixMerger.containsKey(reverseKey)) {
                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
                        if (reverseMatrix != null) {
                            for (int i = 0; i < reverseMatrix[0].length; i++) {
                                for (int j = 0; j < reverseMatrix.length; j++) {
                                    double weightToAdd = reverseMatrix[j][i];
                                    if (weightToAdd != 0) {

                                        //currentWeight += weightToAdd;
                                        currentWeight *= weightToAdd;
                                        //为什么这边调换位置可以跑了？为什么之前不能跑？TODO:????
                                        indexList.add(i);
                                        indexList.add(j);
                                        visited.put(current, true); // 在递归之前标记为已访问
                                        visited.put(nextKey, true); // 在递归之前标记为已访问
                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                        visited.put(nextKey, false); // 回溯后标记为未访问
                                        visited.put(current, false); // 在递归之前标记为已访问
                                        currentWeight /= weightToAdd;
                                        //currentWeight -= weightToAdd;
                                        indexList.remove(indexList.size() - 1);
                                        indexList.remove(indexList.size() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (remaining < TIMES && remaining > 1) {
            // 其他次数
            for (String nextKey : uniqueKeys) {
                String forwardKey = current + "-" + nextKey;
                String reverseKey = nextKey + "-" + current;

                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                    if (matrixMerger.containsKey(forwardKey)) {
                        double[][] matrix = matrixMerger.get(forwardKey);
                        if (matrix != null) {
                            for (int i = 0; i < matrix[0].length; i++) {
                                double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
                                if (weightToAdd != 0) {
                                    //currentWeight += weightToAdd;
                                    currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    visited.put(nextKey, true); // 在递归之前标记为已访问
                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                    visited.put(nextKey, false); // 回溯后标记为未访问
                                    //currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }

                    if (matrixMerger.containsKey(reverseKey)) {
                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
                        if (reverseMatrix != null) {
                            for (int i = 0; i < reverseMatrix.length; i++) {
                                double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
                                if (weightToAdd != 0) {
                                    //currentWeight += weightToAdd;
                                    currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    visited.put(nextKey, true); // 在递归之前标记为已访问
                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
                                    visited.put(nextKey, false); // 回溯后标记为未访问
                                    //currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }
                }
            }
        } else if (remaining == 1 && visited.size() >= (TIMES-1)) {
            // 最后一次
            String firstNode = path.get(0);
            String forwardKey = current + "-" + firstNode;
            String reverseKey = firstNode + "-" + current;

            if ((matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
                if (matrixMerger.containsKey(forwardKey)) {
                    Integer index = indexList.get(0);
                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();

                    double[][] matrix = matrixMerger.get(forwardKey);
                    if (matrix != null) {
                        for (int i = 0; i < matrix[0].length; i++) {
                            double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
                            if (weightToAdd != 0) {
                                if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                    // currentWeight += weightToAdd;
                                    currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                    // currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }
                }

                if (matrixMerger.containsKey(reverseKey)) {
                    Integer index = indexList.get(0);
                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();

                    double[][] reverseMatrix = matrixMerger.get(reverseKey);
                    if (reverseMatrix != null) {
                        for (int i = 0; i < reverseMatrix.length; i++) {
                            double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
                            if (weightToAdd != 0) {
                                if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
                                    // currentWeight += weightToAdd;
                                    currentWeight *= weightToAdd;
                                    indexList.add(i);
                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
                                    // currentWeight -= weightToAdd;
                                    currentWeight /= weightToAdd;
                                    indexList.remove(indexList.size() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        // 回溯
        path.remove(path.size() - 1); // 移除当前节点
    }

//    private static void dfs(Set<String> uniqueKeys, Map<String, double[][]> matrixMerger, String current, int remaining, Map<String, Boolean> visited,
//                            List<String> path, List<Integer> indexList, List<PathResult> results, double currentWeight) {
//        path.add(current); // 添加当前节点到路径中
//
//        if (remaining == 0) { // 如果没有剩余的节点，保存路径结果
//            results.add(new PathResult(new ArrayList<>(path), new ArrayList<>(indexList), currentWeight));
//        } else if (remaining == 4) {
//            // 第一次
//            for (String nextKey : uniqueKeys) {
//                String forwardKey = current + "-" + nextKey;
//                String reverseKey = nextKey + "-" + current;
//
//                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
//                    if (matrixMerger.containsKey(forwardKey)) {
//                        double[][] matrix = matrixMerger.get(forwardKey);
//                        if (matrix != null) {
//                            for (int i = 0; i < matrix.length; i++) {
//                                for (int j = 0; j < matrix.length; j++) {
//                                    double weightToAdd = matrix[i][j];
//                                    if (weightToAdd != 0) {
//                                        currentWeight *= weightToAdd;
//                                    //currentWeight += weightToAdd;
//                                        indexList.add(i);
//                                        indexList.add(j);
//                                        visited.put(current, true); // 在递归之前标记为已访问
//                                        visited.put(nextKey, true); // 在递归之前标记为已访问
//                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
//                                        visited.put(nextKey, false); // 回溯后标记为未访问
//                                        visited.put(current, false); // 在递归之前标记为已访问
//                                        currentWeight /= weightToAdd;
//                                   // currentWeight -= weightToAdd;
//                                        indexList.remove(indexList.size() - 1);
//                                        indexList.remove(indexList.size() - 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    if (matrixMerger.containsKey(reverseKey)) {
//                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
//                        if (reverseMatrix != null) {
//                            for (int i = 0; i < reverseMatrix.length; i++) {
//                                for (int j = 0; j < reverseMatrix.length; j++) {
//                                    double weightToAdd = reverseMatrix[j][i];
//                                    if (weightToAdd != 0) {
//                                    //currentWeight += weightToAdd;
//                                        currentWeight *= weightToAdd;
//                                        indexList.add(j);
//                                        indexList.add(i);
//                                        visited.put(current, true); // 在递归之前标记为已访问
//                                        visited.put(nextKey, true); // 在递归之前标记为已访问
//                                        dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
//                                        visited.put(nextKey, false); // 回溯后标记为未访问
//                                        visited.put(current, false); // 在递归之前标记为已访问
//                                        currentWeight /= weightToAdd;
//                                    //currentWeight -= weightToAdd;
//                                        indexList.remove(indexList.size() - 1);
//                                        indexList.remove(indexList.size() - 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else if (remaining < 4 && remaining > 1) {
//            // 其他次数
//            for (String nextKey : uniqueKeys) {
//                String forwardKey = current + "-" + nextKey;
//                String reverseKey = nextKey + "-" + current;
//
//                if (!current.equals(nextKey) && !visited.get(nextKey) && (matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
//                    if (matrixMerger.containsKey(forwardKey)) {
//                        double[][] matrix = matrixMerger.get(forwardKey);
//                        if (matrix != null) {
//                            for (int i = 0; i < matrix.length; i++) {
//                                double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
//                                if (weightToAdd != 0) {
//                                //currentWeight += weightToAdd;
//                                    currentWeight *= weightToAdd;
//                                    indexList.add(i);
//                                    visited.put(nextKey, true); // 在递归之前标记为已访问
//                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
//                                    visited.put(nextKey, false); // 回溯后标记为未访问
//                                // currentWeight -= weightToAdd;
//                                    currentWeight /= weightToAdd;
//                                    indexList.remove(indexList.size() - 1);
//                                }
//                            }
//                        }
//                    }
//
//                    if (matrixMerger.containsKey(reverseKey)) {
//                        double[][] reverseMatrix = matrixMerger.get(reverseKey);
//                        if (reverseMatrix != null) {
//                            for (int i = 0; i < reverseMatrix.length; i++) {
//                                double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
//                                if (weightToAdd != 0) {
//                                //currentWeight += weightToAdd;
//                                    currentWeight *= weightToAdd;
//                                    indexList.add(i);
//                                    visited.put(nextKey, true); // 在递归之前标记为已访问
//                                    dfs(uniqueKeys, matrixMerger, nextKey, remaining - 1, visited, path, indexList, results, currentWeight);
//                                    visited.put(nextKey, false); // 回溯后标记为未访问
//                                //currentWeight -= weightToAdd;
//                                    currentWeight /= weightToAdd;
//                                    indexList.remove(indexList.size() - 1);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else if (remaining == 1 && visited.size() >= 3) {
//            // 最后一次
//            String firstNode = path.get(0);
//            String forwardKey = current + "-" + firstNode;
//            String reverseKey = firstNode + "-" + current;
//
//            if ((matrixMerger.containsKey(forwardKey) || matrixMerger.containsKey(reverseKey))) {
//                if (matrixMerger.containsKey(forwardKey)) {
//                    Integer index = indexList.get(0);
//                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();
//
//                    double[][] matrix = matrixMerger.get(forwardKey);
//                    if (matrix != null) {
//                        for (int i = 0; i < matrix.length; i++) {
//                            double weightToAdd = matrix[indexList.get(indexList.size()-1)][i];
//                            if (weightToAdd != 0) {
//                                if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
//                                   // currentWeight += weightToAdd;
//                                   currentWeight *= weightToAdd;
//                                    indexList.add(i);
//                                    dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
//                                   // currentWeight -= weightToAdd;
//                                   currentWeight /= weightToAdd;
//                                    indexList.remove(indexList.size() - 1);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (matrixMerger.containsKey(reverseKey)) {
//                    Integer index = indexList.get(0);
//                    int realNodeId = sketchToRealMap.get(Integer.parseInt(firstNode)).get(index).getRealNodeId();
//
//                    double[][] reverseMatrix = matrixMerger.get(reverseKey);
//                    if (reverseMatrix != null) {
//                        for (int i = 0; i < reverseMatrix.length; i++) {
//                            double weightToAdd = reverseMatrix[i][indexList.get(indexList.size()-1)];
//                            if (weightToAdd != 0) {
//                                 if (realNodeId == sketchToRealMap.get(Integer.parseInt(firstNode)).get(i).getRealNodeId()) {
//                                    // currentWeight += weightToAdd;
//                                     currentWeight *= weightToAdd;
//                                     indexList.add(i);
//                                     dfs(uniqueKeys, matrixMerger, firstNode, 0, visited, path, indexList, results, currentWeight);
//                                    // currentWeight -= weightToAdd;
//                                     currentWeight /= weightToAdd;
//                                     indexList.remove(indexList.size() - 1);
//                                 }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        // 回溯
//        path.remove(path.size() - 1); // 移除当前节点
//    }


}