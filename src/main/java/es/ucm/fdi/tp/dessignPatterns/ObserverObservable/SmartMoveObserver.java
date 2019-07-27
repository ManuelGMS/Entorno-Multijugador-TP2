package es.ucm.fdi.tp.dessignPatterns.ObserverObservable;

public interface SmartMoveObserver {
	public void onStart();
	public void onEnd(boolean success, long time, int nodes, double value);
}
