# Garder les numéros de ligne pour pouvoir lire les rapports de plantage dans la Play Console
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Sécurité : le rappel planifié (WorkManager) est recréé par son nom
-keep class com.laurena.comprendremonchien.RappelWorker { *; }

# Sécurité : l'historique et les modèles de données restent intacts
-keep class com.laurena.comprendremonchien.BilanSauvegarde { *; }
-keepclassmembers enum com.laurena.comprendremonchien.** { *; }

# Correction du plantage au démarrage : base de données interne de WorkManager
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-keep class androidx.work.impl.WorkDatabase_Impl { *; }