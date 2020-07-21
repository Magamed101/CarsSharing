package ru.itis.carssharing.ui.home.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car.*
import ru.itis.carssharing.entity.Car

class CarHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        data: Car,
        click: (Car) -> Unit
    ) {
        tv_name.text = data.name
        tv_price.text = data.price + " р/час"
        cv_container.setOnClickListener { click(data) }
    }
}