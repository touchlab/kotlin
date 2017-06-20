/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.MultiRangeReference
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.AbstractCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.CollectionLiteralResolver
import java.util.*

class KtCollectionLiteralReference(expression: KtCollectionLiteralExpression) : AbstractCollectionLiteralReference<KtCollectionLiteralExpression>(expression) {
    companion object {
        val COLLECTION_LITERAL_CALL_NAMES = CollectionLiteralResolver.PRIMITIVE_TYPE_TO_ARRAY.values + CollectionLiteralResolver.ARRAY_OF_FUNCTION
    }

    override fun getTargetDescriptors(context: BindingContext): Collection<DeclarationDescriptor> {
        val resolvedCall = context[BindingContext.COLLECTION_LITERAL_CALL, element]
        return listOfNotNull(resolvedCall?.resultingDescriptor)
    }

    override val resolvesByNames: Collection<Name>
        get() = COLLECTION_LITERAL_CALL_NAMES
}

abstract class AbstractCollectionLiteralReference<T : AbstractCollectionLiteralExpression>(expression: T) : KtSimpleReference<T>(expression), MultiRangeReference {

    override fun getRangeInElement(): TextRange = element.textRange.shiftRight(-element.textOffset)

    override fun getRanges(): List<TextRange> {
        val list = ArrayList<TextRange>()

        var textRange = element.leftBracket!!.textRange
        val lBracketRange = textRange.shiftRight(-expression.textOffset)

        list.add(lBracketRange)

        val rBracket = element.rightBracket
        if (rBracket != null) {
            textRange = rBracket.textRange
            val rBracketRange = textRange.shiftRight(-expression.textOffset)
            list.add(rBracketRange)
        }

        return list
    }
}