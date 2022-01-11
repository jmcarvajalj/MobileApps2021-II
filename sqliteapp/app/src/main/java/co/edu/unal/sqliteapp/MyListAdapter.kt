package co.edu.unal.sqliteapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Activity, private val id: Array<String>, private val name: Array<String>, private val email: Array<String>, private val url: Array<String>, private val phone: Array<String>, private val products: Array<String>, private val type: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val nameText = rowView.findViewById(R.id.textViewName) as TextView
        val emailText = rowView.findViewById(R.id.textViewEmail) as TextView
        val urlText = rowView.findViewById(R.id.textViewUrl) as TextView
        val phoneText = rowView.findViewById(R.id.textViewPhone) as TextView
        val productsText = rowView.findViewById(R.id.textViewProducts) as TextView
        val typeText = rowView.findViewById(R.id.textViewType) as TextView

        idText.text = "Id de la empresa: ${id[position]}"
        nameText.text = "Nombre de la empresa: ${name[position]}"
        emailText.text = "Email de la empresa: ${email[position]}"
        urlText.text = "URL de la empresa: ${url[position]}"
        phoneText.text = "Telefono de la empresa: ${phone[position]}"
        productsText.text = "Productos/Servicios de la empresa: ${products[position]}"
        typeText.text = "Clasificación de la empresa: ${type[position]}"
        return rowView
    }
}