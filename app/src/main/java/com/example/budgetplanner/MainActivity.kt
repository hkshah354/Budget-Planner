package com.example.budgetplanner

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetplanner.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private val expenses = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupRecyclerView()

        binding.addExpenseButton.setOnClickListener {
            addExpense()
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.categorySpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Optionally handle item selection
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optionally handle no item selected
            }
        }
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(expenses)
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.expensesRecyclerView.adapter = expenseAdapter
    }

    private fun addExpense() {
        val description = binding.expenseDescription.text.toString()
        val amount = binding.expenseAmount.text.toString().toDoubleOrNull()
        val category = binding.categorySpinner.selectedItem.toString()

        if (description.isNotEmpty() && amount != null) {
            val expense = Expense(description, amount, category)
            expenses.add(expense)
            expenseAdapter.notifyItemInserted(expenses.size - 1)
            updateTotalExpense()
            binding.expenseDescription.text.clear()
            binding.expenseAmount.text.clear()
            updateUI()
        }
    }

    private fun updateTotalExpense() {
        val total = expenses.sumByDouble { it.amount }
        binding.totalExpense.text = "Total Expense: $$total"
    }

    private fun updateUI() {
        if (expenses.isEmpty()) {
            binding.expensesRecyclerView.visibility = View.GONE
            binding.noExpensesMessage.visibility = View.VISIBLE
        } else {
            binding.expensesRecyclerView.visibility = View.VISIBLE
            binding.noExpensesMessage.visibility = View.GONE
        }
    }
}
