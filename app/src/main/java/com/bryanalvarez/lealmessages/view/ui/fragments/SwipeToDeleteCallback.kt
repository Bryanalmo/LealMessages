package com.bryanalvarez.lealmessages.view.ui.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bryanalvarez.lealmessages.R
import com.bryanalvarez.lealmessages.view.adapters.TransactionsAdapter


class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback{


    var adapter: TransactionsAdapter
    var icon: Drawable
    var background: ColorDrawable;

    constructor(adapter: TransactionsAdapter, context: Context) : super(0,ItemTouchHelper.LEFT) {
        this.adapter = adapter
        icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_36)!!
        background = ColorDrawable(Color.RED)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        var itemView = viewHolder.itemView
        var backgroundCornerOffset = 20

        var iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2
        var iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2
        var iconBottom = iconTop + icon.getIntrinsicHeight()

        if (dX > 0) {

            var iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            var iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                itemView.getLeft() + (dX.toInt()) + backgroundCornerOffset,
                itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            var iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth()
            var iconRight = itemView.getRight() - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.getRight() + (dX.toInt()) - backgroundCornerOffset,
                itemView.getTop(), itemView.getRight(), itemView.getBottom())
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
    }

}