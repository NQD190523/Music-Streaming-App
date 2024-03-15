import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.appealic.Activity_Signin
import com.project.appealic.Activiy_Singup
import com.project.appealic.databinding.ActivityWelcome2Binding
import com.project.appealic.databinding.ActivityWelcomeBinding

class Activity_welcome2 : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng DataBindingUtil để gắn kết layout
        binding = ActivityWelcome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện khi nút Sign In được nhấn
        binding.btnSignin.setOnClickListener {
            val intent = Intent(this, Activity_Signin::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện khi nút Sign Up được nhấn
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, Activiy_Singup::class.java)
            startActivity(intent)
        }
    }
}
