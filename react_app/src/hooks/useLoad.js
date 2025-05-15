import { useCallback, useState } from "react"

export const LoadStatus = {
	LOADING: 'LOADING',
	COMPLETED: 'COMPLETED',
	ERROR: 'ERROR',
	CANCELED: 'CANCELED',
	RELOADING: 'RELOADING',
}
export function useLoad(loader, triggers) {
	const [state, setState] = useState(LoadStatus.LOADING)
	const callBack = useCallback((...args) => {
		setState(LoadStatus.LOADING)
		return loader(...args)
			.then((data) => {
				setState(LoadStatus.COMPLETED)
				return data
			})
			.catch(() => {
				setState(LoadStatus.ERROR)
			})
	}, [state, ...triggers])

	const cancel = () => setState(LoadStatus.CANCELED)

	const reload = () => setState(LoadStatus.RELOADING)

	return [callBack, state, cancel, reload]
}