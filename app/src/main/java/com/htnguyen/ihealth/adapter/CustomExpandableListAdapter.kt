package com.htnguyen.ihealth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.htnguyen.ihealth.R


class CustomExpandableListAdapter(
    private val context: Context, private val expandableListTitle: List<String>,
    private val expandableListDetail: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return expandableListDetail[expandableListTitle[listPosition]]
            ?.get(expandedListPosition) ?: 0
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return expandableListDetail[expandableListTitle[listPosition]]
            ?.size ?: 0
    }

    override fun getGroup(listPosition: Int): Any {
        return expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View? {
        var convertView = p2

        if(convertView ==  null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.item_list_group, null)
        }

        val txtGroupTitle = convertView?.findViewById<TextView>(R.id.listTitle)
        txtGroupTitle?.text = getGroup(p0).toString()
        return convertView
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View? {
        var convertView = p3

        if(convertView ==  null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.item_list_item, null)
        }

        val txtChildTitle = convertView?.findViewById<TextView>(R.id.expandedListItem)
        txtChildTitle?.text = getChild(p0, p1).toString()
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}