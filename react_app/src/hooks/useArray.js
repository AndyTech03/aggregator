import React, { useEffect, useState } from "react"
import Cookies from 'js-cookie';

export default function useArray(cookiesName=false) {
	const [items, setItems] = useState([])
	const [loading, setLoading] = useState(true)
	useEffect(() => {
		if (cookiesName === false)
			return;
		const arrayItems = Cookies.get(cookiesName)
		if (arrayItems == null)
			return;
		setItems(JSON.parse(arrayItems))
		setLoading(false)
	}, [cookiesName, setLoading])
	useEffect(() => {
		if (cookiesName === false || loading)
			return;
		Cookies.set(cookiesName, JSON.stringify(items))
	}, [items, loading, cookiesName])
	const addItem = (item) => {
		setItems(prev => [item, ...prev])
	}
	const delItem = (item) => {
		setItems(prev => prev.filter(c => c !== item))
	}
	const contains = (item) => items.includes(item)
	
	return {items, addItem, delItem, setItems, contains}
}