package com.ketchupzz.francingsfootwearadmin.model.messages

import com.ketchupzz.francingsfootwearadmin.model.customer.Customer

class CustomerWithMessage(val customer: Customer,val messages: List<Messages>) {
}