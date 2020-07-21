package ru.itis.carssharing.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.itis.carssharing.R
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.util.applyDiff

class CarsAdapter(
    private var list: ArrayList<Car>,
    private val click: (Car) -> Unit
) : RecyclerView.Adapter<CarHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_car,
                parent,
                false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CarHolder, position: Int) =
        holder.bind(
            list[position],
            click
        )

    fun updateList(newList: ArrayList<Car>) {
        applyDiff(
            oldList = list,
            newList = newList,
            areItemsTheSame = { old, new -> old == new },
            areContentsTheSame = { old, new -> old == new }
        )
        this.list = newList
    }

}