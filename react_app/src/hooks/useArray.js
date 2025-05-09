import React, { useEffect } from "react"
import useValue from "./useValue";


export default function useArray(cookiesName=false, defaultValue=[]) {
	const [items, setItems] = useValue(cookiesName, defaultValue)
	useEffect(() => {
		if (items == null)
			setItems([])
	}, [items])
	const addItem = (item) => {
		setItems(prev => [item, ...prev])
	}
	const delItem = (item) => {
		setItems(prev => prev.filter(c => c !== item))
	}
	const contains = (item) => items.includes(item)
	
	return {items, addItem, delItem, setItems, contains}
}