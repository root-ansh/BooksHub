package io.github.curioustools.bookshub.baseutils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

abstract class BaseComposeFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).also {
            it.setViewCompositionStrategy(getViewCompositionStrategy())
            it.setContent{
                Ui(savedInstanceState)
            }
        }
    }

    open fun getViewCompositionStrategy(): ViewCompositionStrategy {
        return ViewCompositionStrategy.DisposeOnDetachedFromWindow
    }

    @Composable
    abstract fun Ui(bundle: Bundle?)
}