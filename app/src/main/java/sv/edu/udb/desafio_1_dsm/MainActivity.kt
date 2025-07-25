package sv.edu.udb.desafio_1_dsm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etTotal: EditText
    private lateinit var etPersonas: EditText
    private lateinit var rgPropina: RadioGroup
    private lateinit var swIVA: Switch
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpiar: Button
    private lateinit var llResultados: LinearLayout
    private lateinit var tvPropinaCalculada: TextView
    private lateinit var tvTotalPagar: TextView
    private lateinit var tvPorPersona: TextView
    private lateinit var etCustomTip: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etTotal = findViewById(R.id.etTotal)
        etPersonas = findViewById(R.id.etPersonas)
        rgPropina = findViewById(R.id.rgPropina)
        swIVA = findViewById(R.id.swIVA)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        llResultados = findViewById(R.id.llResultados)
        tvPropinaCalculada = findViewById(R.id.tvPropinaCalculada)
        tvTotalPagar = findViewById(R.id.tvTotalPagar)
        tvPorPersona = findViewById(R.id.tvPorPersona)
        etCustomTip = findViewById(R.id.etCustomTip)

        rgPropina.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbOtro) {
                etCustomTip.visibility = View.VISIBLE
            } else {
                etCustomTip.visibility = View.GONE
                etCustomTip.text.clear()
            }
        }

        btnCalcular.setOnClickListener {
            if (validateInput()) {
                calcularResultados()
            }
        }

        btnLimpiar.setOnClickListener {
            limpiar()
        }
    }

    private fun validateInput(): Boolean {
        etTotal.error = null
        etPersonas.error = null
        etCustomTip.error = null

        val totalStr = etTotal.text.toString()
        val personasStr = etPersonas.text.toString()
        val customTipStr = etCustomTip.text.toString()

        var isValid = true
        var errorMessage = ""

        val total = totalStr.toDoubleOrNull()
        if (total == null || total <= 0) {
            etTotal.error = getString(R.string.error_monto_total_invalido)
            isValid = false
        }

        val personas = personasStr.toIntOrNull()
        if (personas == null || personas <= 0) {
            etPersonas.error = getString(R.string.error_num_personas_invalido)
            isValid = false
        }

        if (rgPropina.checkedRadioButtonId == R.id.rbOtro) {
            val customTip = customTipStr.toDoubleOrNull()
            if (customTip == null || customTip <= 0) {
                etCustomTip.error = getString(R.string.error_propina_personalizada_invalida)
                isValid = false
            }
        }


        if (!isValid) {
            if (errorMessage.isNotEmpty()) {
                mostrarMensajeError(getString(R.string.alert_title_error), errorMessage)
            } else {
                mostrarMensajeError(
                    getString(R.string.alert_title_error),
                    getString(R.string.alert_message_campos_invalidos)
                )
            }
            llResultados.visibility = View.GONE
            return false
        }

        return true
    }

    private fun calcularResultados() {
        val total = etTotal.text.toString().toDouble()
        val personas = etPersonas.text.toString().toInt()

        val porcentajePropina = when (rgPropina.checkedRadioButtonId) {
            R.id.rb10 -> 0.10
            R.id.rb15 -> 0.15
            R.id.rb20 -> 0.20
            R.id.rbOtro -> etCustomTip.text.toString().toDouble() / 100
            else -> 0.0
        }

        var totalConIva = total
        if (swIVA.isChecked) {
            totalConIva *= 1.16
        }

        val propina = total * porcentajePropina
        val totalConPropina = totalConIva + propina
        val porPersona = totalConPropina / personas

        tvPropinaCalculada.text = getString(R.string.resultadoPropina, propina)
        tvTotalPagar.text = getString(R.string.resultadoTotal, totalConPropina)
        tvPorPersona.text = getString(R.string.resultadoPorPersona, porPersona)

        llResultados.visibility = View.VISIBLE
    }

    private fun mostrarMensajeError(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_ok), null)
            .show()
    }

    private fun limpiar() {
        etTotal.text.clear()
        etPersonas.text.clear()
        rgPropina.clearCheck()
        swIVA.isChecked = false
        etCustomTip.text.clear()
        etCustomTip.visibility = View.GONE
        llResultados.visibility = View.GONE


        etTotal.error = null
        etPersonas.error = null
        etCustomTip.error = null
    }

}