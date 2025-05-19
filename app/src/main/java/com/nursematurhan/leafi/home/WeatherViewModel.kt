import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.nursematurhan.leafi.data.api.RetrofitInstance
import com.nursematurhan.leafi.data.api.WeatherApi
import com.nursematurhan.leafi.home.WeatherResponse

class WeatherViewModel : ViewModel() {
    private val _weather = mutableStateOf<WeatherResponse?>(null)
    val weather: State<WeatherResponse?> = _weather

    fun loadWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getWeatherByCity(city, apiKey)
                _weather.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadWeatherByCoords(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCoordinates(lat, lon, apiKey)
                _weather.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather by coords", e)
            }
        }
    }
}