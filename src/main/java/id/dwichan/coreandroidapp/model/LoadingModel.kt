package id.dwichan.coreandroidapp.model

import id.dwichan.coreandroidapp.util.State

class LoadingModel: Model {
    override fun getType(): Int = State.STATE_LOADING
}