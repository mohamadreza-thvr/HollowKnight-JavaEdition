package com.hallowKnight.gameplay.boss;

public enum BossState {
    IDLE,
    RUN_ANTIC,      // آماده‌سازی حمله
    MACE_SLAM,      // کوبیدن گرز (نزدیک)
    CHARGE_RUN,     // دویدن/هجوم افقی
    OFFENSIVE_LEAP, // پرش هجومی (دور و متوسط)
    DEFENSIVE_LEAP, // پرش دفاعی (تصادفی)
    JUMP_ANTIC,     // آماده‌سازی پرش
    JUMP,           // در هوا
    LAND,           // فرود
    LEAP_SLAM,
    TURN,           // چرخش
    STUN,           // گیج شده
    STUN_RECOVER,   // بازیابی از stun
    DEATH_HIT,
    DEATH_FALL,     // سقوط
    DEATH_LAND      // روی زمین (مرگ کامل)
}
