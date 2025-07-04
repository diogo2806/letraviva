package br.com.valenstech.letraviva.ui.planos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.model.ReadingPlan

class PlansAdapter(private val plans: List<ReadingPlan>) : RecyclerView.Adapter<PlansAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount() = plans.size

    inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(android.R.id.text1)
        fun bind(plan: ReadingPlan) {
            text.text = plan.title
        }
    }
}
