# ArkeoMag Pro NoLogin v2 Classic

Temiz Android Studio / Gradle projesi.

## Hedef
Eski APK'deki görünüm mantığını aşama aşama taşıyan, kullanıcı girişi/Firebase/Play Integrity/lisans kontrolü olmayan temiz ArkeoMag sürümü.

## Bu v2 içinde
- Native Android SensorManager ile manyetometre, ivmeölçer, gyro
- Dedektör ekranı
- Kalibrasyon ekranı
- Live Scan
- Area Scan
- Sonar / 3D temsilî ModelSurfaceView
- LineGraphView
- ColumnGraphView
- Titreşim alarmı
- CSV kayıt
- Bluetooth eşleşmiş cihaz listeleme
- Eski APK varlıkları referans için `legacy_reference/` içinde
- Eski `assets/` içeriği app assets içine taşındı
- GitHub Actions APK build

## Bilerek yok
- Login/Register
- Firebase Auth
- Play Integrity
- PairIP license check
- Billing

## GitHub Actions
Push sonrası Actions sekmesinden APK artifact indir.

## Aşamalar
v2: MainMenu + Dedektör + Kalibrasyon + Live/Area/Sonar temel görünüm.
v3: Eski XML görünümünün daha birebir portu.
v4: OpenGL ModelSurfaceView shader/marching-cubes portu.
