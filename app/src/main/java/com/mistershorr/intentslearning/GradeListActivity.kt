package com.mistershorr.intentslearning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_grade_list.*

class GradeListActivity : AppCompatActivity() {
    companion object {
        val TAG = "GradeListActivity"
        val gson = Gson()
        val userId = Backendless.UserService.CurrentUser().userId
    }

    private var gradesList : List<Grade?>? = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_list)
        readAllUserGrades()



        button_gradelist_read.setOnClickListener {
            readAllUserGrades()
        }
        button_gradelist_create.setOnClickListener {
            createNewGrade()
        }
        button_gradelist_delete.setOnClickListener() {
            deleteFirstGrade()
        }
        button_gradelist_update.setOnClickListener() {
            updateFirstGrade()
        }

    }

    private fun updateFirstGrade() {
        if(!gradesList.isNullOrEmpty()) {
            val grade = gradesList?.get(0)
            grade?.assignment = "Read the entire Bible"

            Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
                override fun handleResponse(response: Grade?) {
                    Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show();
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, "handleFault: ${fault.detail}")
                }
            })
        }
    }
    private fun deleteFirstGrade() {
        if(!gradesList.isNullOrEmpty()){
            Backendless.Data.of(Grade::class.java).remove(gradesList?.get(0), object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    Toast.makeText(this@GradeListActivity, "Grade Deleted", Toast.LENGTH_SHORT).show();
                    readAllUserGrades()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, "handleFault: ${fault.detail}")
                }

            })
        }
    }

    private fun createNewGrade() {
        Log.d(TAG, "createNewGrade: $userId")
        val grade = Grade(
            assignment = "Read Pg. 1 - 1000 of the dictionary",
            studentName = "Connor"
        )
        grade.ownerId = userId
        Log.d(TAG, "createNewGrade: ")

        Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
            override fun handleResponse(response: Grade?) {
                Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show();
            }

            override fun handleFault(fault: BackendlessFault) {
                Log.d(TAG, "handleFault: ${fault.detail}")
            }
        })
    }



    private fun readAllUserGrades() {

        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

// TODO: make a whole recyclerview layout and stuff for the grades

// for now, log a list of all the grades
        Backendless.Data.of(Grade::class.java).find(
            queryBuilder,
            object : AsyncCallback<List<Grade?>?> {
                override fun handleResponse(foundGrades: List<Grade?>?) {
// all Grade instances have been found
                    val json = gson.toJson(foundGrades)
                    gradesList = foundGrades
                    Log.d(TAG, "handleResponse: $json")
                    Toast.makeText(this@GradeListActivity,"handleResponse: $json" , Toast.LENGTH_SHORT).show()

                }

                override fun handleFault(fault: BackendlessFault) {
// an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d(TAG, "handleFault: " + fault.message)
                    Toast.makeText(this@GradeListActivity,"handleFault: " + fault.message, Toast.LENGTH_SHORT).show()
                }
            })
    }



// code below here can't guarantee that the data has been retrieved.
// this is executed right away after the async call goes out, but might be
// before the async call comes back

    }
