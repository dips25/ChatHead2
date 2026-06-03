Welcome to the ChatHead2 wiki!

## Add it in your root settings.gradle at the end of repositories:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

```

## Add dependency
`implementation("com.github.dips25:ChatHead2:v1.1")`

## Use chathead in your activity/fragment

```
public class MainActivity extends AppCompatActivity implements ChatHeadListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startChatHead();
    }

    private void startChatHead() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                startActivityForResult(intent, 1001);
            } else {
                // Permission already granted
                startChatHeadService();
            }
        }
    }

    private void startChatHeadService() {

        ChatHead chatHead = new ChatHead(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission granted
                    startChatHeadService();
                } else {
                    Toast.makeText(this,
                            "Overlay permission denied",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onChatHeadClicked() {

        Log.d(MainActivity.class.getName(), "onChatHeadClicked");
    }
}
```
## Edit chat_head_layout.xml

```
<ImageView android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="@drawable/circle_drawable"
    android:scaleType="centerInside"
    xmlns:android="http://schemas.android.com/apk/res/android" />
```

<img width="368" height="832" alt="Screen_recording_20260603_154458-ezgif com-video-to-gif-converter" src="https://github.com/user-attachments/assets/63c729c0-832f-4e7c-9b10-bf38049f5315" />

<img width="720" height="1650" alt="Screenshot_20260603_154645" src="https://github.com/user-attachments/assets/6e19c66a-f0d4-425a-9f9a-baa57503bb62" />
<img width="720" height="1650" alt="Screenshot_20260603_154719" src="https://github.com/user-attachments/assets/faaad119-3982-4be0-9187-343a189dc460" />
<img width="720" height="1650" alt="Screenshot_20260603_154734" src="https://github.com/user-attachments/assets/ff6e4918-f95a-4ca1-ae38-f55716e609ca" />






