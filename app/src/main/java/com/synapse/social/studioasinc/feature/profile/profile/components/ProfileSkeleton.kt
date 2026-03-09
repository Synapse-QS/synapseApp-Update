package com.synapse.social.studioasinc.feature.profile.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.synapse.social.studioasinc.feature.shared.theme.Spacing
import com.synapse.social.studioasinc.ui.components.ShimmerBox
import com.synapse.social.studioasinc.ui.components.ShimmerCircle

@Composable
fun ProfileHeaderSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(0.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = Spacing.Medium)
                    .offset(y = 55.dp) // half of 110.dp
            ) {
                ShimmerCircle(size = 110.dp)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium)
        ) {
            ShimmerBox(
                modifier = Modifier
                    .width(150.dp)
                    .height(28.dp)
            )
            Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
            ShimmerBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.Small))
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(40.dp)
                                .height(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ShimmerBox(
                            modifier = Modifier
                                .width(60.dp)
                                .height(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp) // Assuming rounded expressive buttons
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))
        }
    }
}

@Composable
fun PostCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.SmallMedium, vertical = Spacing.Small)
        ) {
            Box(
                modifier = Modifier.width(48.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                ShimmerCircle(size = 48.dp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Header text placeholder
                ShimmerBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Content text placeholders
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Media placeholder
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Interaction bar placeholder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(32.dp)
                                .height(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PhotoGridSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                repeat(3) {
                    ShimmerBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
