package `in`.iot.lab.dmscanner.ui

import `in`.iot.lab.dmscanner.model.Identifiable
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class GenericAdapter<T, B : ViewBinding>(
        val bindingProvider: (LayoutInflater, ViewGroup, Boolean) -> B,
        val itemHandler: (T, B) -> Unit) : ListAdapter<T, GenericAdapter.GenericViewModel<T, B>>(GenericCallBack<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewModel<T, B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = bindingProvider(layoutInflater, parent, false)
        return GenericViewModel(binding)
    }

    override fun onBindViewHolder(holder: GenericViewModel<T, B>, position: Int) {
        val item = getItem(position)
        itemHandler(item, holder.binding)
    }

    class GenericViewModel<T, B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

    private class GenericCallBack<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem is Identifiable && newItem is Identifiable){
                oldItem.id==newItem.id
            }else
                oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }
}