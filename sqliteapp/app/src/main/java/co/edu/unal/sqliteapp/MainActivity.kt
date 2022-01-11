package co.edu.unal.sqliteapp

//import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    //method for saving records in database
    fun saveRecord(view: View){
        val idCompany = id.text.toString()
        val name = nombre_empresa.text.toString()
        val email = contact_email.text.toString()
        val url = page_url.text.toString()
        val phone = phone_number.text.toString()
        val products = products_and_services.text.toString()
        val type = type_of_company.text.toString()
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        if(idCompany.trim()!="" && name.trim()!="" && email.trim()!="" && url.trim()!="" && phone.trim()!="" && products.trim()!="" && type.trim()!=""){
            val status = databaseHandler.addEmployee(EmpModelClass(Integer.parseInt(idCompany), name, email, url, Integer.parseInt(phone), products, type))
            if(status > -1){
                Toast.makeText(applicationContext,"Datos guardados",Toast.LENGTH_LONG).show()
                id.text.clear()
                nombre_empresa.text.clear()
                contact_email.text.clear()
                page_url.text.clear()
                phone_number.text.clear()
                products_and_services.text.clear()
                type_of_company.text.clear()
            }
        }else{
            Toast.makeText(applicationContext,"No puedes dejar datos en blanco",Toast.LENGTH_LONG).show()
        }

    }
    //method for read records from database in ListView
    fun viewRecord(view: View){
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        val empArrayUrl = Array<String>(emp.size){"null"}
        val empArrayPhone = Array<String>(emp.size){"0"}
        val empArrayProducts = Array<String>(emp.size){"null"}
        val empArrayType = Array<String>(emp.size){"null"}
        var index = 0
        for(e in emp){
            empArrayId[index] = e.idCompany.toString()
            empArrayName[index] = e.name
            empArrayEmail[index] = e.email
            empArrayUrl[index] = e.url
            empArrayPhone[index] = e.phone.toString()
            empArrayProducts[index] = e.products
            empArrayType[index] = e.type
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(this,empArrayId,empArrayName,empArrayEmail, empArrayUrl, empArrayPhone, empArrayProducts, empArrayType)
        listView.adapter = myListAdapter
    }
    //method for updating records based on user id
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
        val edtUrl = dialogView.findViewById(R.id.updateUrl) as EditText
        val edtPhone = dialogView.findViewById(R.id.updatePhone) as EditText
        val edtProducts = dialogView.findViewById(R.id.updateProducts) as EditText
        val edtType = dialogView.findViewById(R.id.updateType) as EditText

        dialogBuilder.setTitle("Actualizar Datos")
        dialogBuilder.setMessage("Inserte los siguientes datos")
        dialogBuilder.setPositiveButton("Actualizar", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            val updateUrl = edtUrl.text.toString()
            val updatePhone = edtPhone.text.toString()
            val updateProducts = edtProducts.text.toString()
            val updateType = edtType.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!="" && updateUrl.trim()!="" && updatePhone.trim()!="" && updateProducts.trim()!="" && updateType.trim()!=""){
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail, updateUrl, Integer.parseInt(updatePhone), updateProducts, updateType))
                if(status > -1){
                    Toast.makeText(applicationContext,"Datos actualizados.",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"No puedes dejar datos en blanco.",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
    //method for deleting records based on id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Borrar Datos")
        dialogBuilder.setMessage("Inserte la ID de la empresa a borrar:")
        dialogBuilder.setPositiveButton("Borrar", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(deleteId.trim()!=""){
                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"","", "", 0, "", ""))
                if(status > -1){
                    Toast.makeText(applicationContext,"Datos borrados.",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"No puedes dejar datos en blanco.",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}