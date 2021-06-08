package uz.myapigrid.taskmy

sealed class Status {
    object LOADING : Status()
    object SUCCESS : Status()
    class ERROR(val res: Int) : Status()
}